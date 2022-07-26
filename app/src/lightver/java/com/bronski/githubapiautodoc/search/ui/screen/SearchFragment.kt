package com.bronski.githubapiautodoc.search.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bronski.githubapiautodoc.core.api.data.Repository
import com.bronski.githubapiautodoc.core.ui.BaseFragment
import com.bronski.githubapiautodoc.core.utils.RecyclerItemListener
import com.bronski.githubapiautodoc.core.utils.simpleScan
import com.bronski.githubapiautodoc.databinding.FragmentSearchBinding
import com.bronski.githubapiautodoc.search.ui.DefaultLoadStateAdapter
import com.bronski.githubapiautodoc.search.ui.SearchAdapter
import com.bronski.githubapiautodoc.search.ui.SearchViewModel
import com.bronski.githubapiautodoc.search.ui.TryAgainAction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder

    private val recyclerItemListener = object : RecyclerItemListener {
        override fun onItemClick(itemRepository: Repository) {
            displayToastMessage("Flavor without details")
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy < 0 && !binding.scrollToTop.isShown) {
                binding.scrollToTop.show()
            } else if (dy >= 0 && binding.scrollToTop.isShown) {
                binding.scrollToTop.hide()
            }
        }
    }

    private val searchAdapter = SearchAdapter(listener = recyclerItemListener)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setupSearchInput()
        observeRepositories()
        observeLoadState()
        swipeToRefreshData()
        scrollToTop()
        handleScrollingToTopWhenSearching()
    }

    private fun setUpAdapter() = with(binding) {

        val tryAgainAction: TryAgainAction = { searchAdapter.retry() }
        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)
        val adapterWithLoadState = searchAdapter.withLoadStateFooter(footerAdapter)

        searchRecycler.apply {
            adapter = adapterWithLoadState
            layoutManager = LinearLayoutManager(requireContext())
            (itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            loadStateView,
            swipeToRefresh,
            tryAgainAction
        )
    }

    private fun setupSearchInput() {
        binding.searchEditText.addTextChangedListener {
            viewModel.setSearchBy(it.toString())
        }
    }

    private fun swipeToRefreshData() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.swipeToUpdate()
        }
    }

    private fun scrollToTop() {
        binding.scrollToTop.setOnClickListener {
            binding.searchRecycler.postDelayed({
                binding.searchRecycler.smoothScrollToPosition(0)
            }, 250)
        }
    }

    private fun observeRepositories() {
        lifecycleScope.launch {
            viewModel.repositoriesFlow.collectLatest {
                searchAdapter.submitData(it)
            }
        }
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            searchAdapter.loadStateFlow.debounce(200).collectLatest {
                mainLoadStateHolder.bind(it.refresh)
            }
        }
    }

    private fun handleScrollingToTopWhenSearching() = lifecycleScope.launch {
        getRefreshLoadStateFlow()
            .simpleScan(count = 2)
            .collectLatest { (previousState, currentState) ->
                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                    binding.searchRecycler.scrollToPosition(0)
                }
            }
    }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return searchAdapter.loadStateFlow
            .map { it.refresh }
    }

    override fun getViewBinding() = FragmentSearchBinding.inflate(layoutInflater)
}