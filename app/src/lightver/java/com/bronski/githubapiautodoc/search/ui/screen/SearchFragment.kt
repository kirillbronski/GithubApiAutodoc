package com.bronski.githubapiautodoc.search.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bronski.githubapiautodoc.core.api.data.SearchResponseResult
import com.bronski.githubapiautodoc.core.state.ViewState
import com.bronski.githubapiautodoc.core.ui.BaseFragment
import com.bronski.githubapiautodoc.core.utils.RecyclerItemListener
import com.bronski.githubapiautodoc.databinding.FragmentSearchBinding
import com.bronski.githubapiautodoc.search.ui.SearchAdapter
import com.bronski.githubapiautodoc.search.ui.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchViewModel>()

    private val recyclerItemListener = object : RecyclerItemListener {
        override fun onItemClick(itemRepository: SearchResponseResult) {
            displayToastMessage("Without details version")
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy < 0 && !binding.scrollToTop.isShown)
                binding.scrollToTop.show();
            else if (dy >= 0 && binding.scrollToTop.isShown)
                binding.scrollToTop.hide();
        }
    }

    private val searchAdapter = SearchAdapter(listener = recyclerItemListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        textChangedListener()
        checkViewState()
        swipeToRefreshData()
        scrollToTop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkViewState() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect {
                when (it) {
                    is ViewState.SuccessState -> {
                        searchAdapter.submitList(it.listRepo)
                        hideProgressIndicator(binding.includedProgressBar.progressBar)
                    }
                    is ViewState.LoadingState -> {
                        showProgressIndicator(binding.includedProgressBar.progressBar)
                    }
                    is ViewState.ErrorState -> {
                        displayToastMessage(it.message.toString())
                        hideProgressIndicator(binding.includedProgressBar.progressBar)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setUpAdapter() = with(binding) {
        searchRecycler.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }

    private fun textChangedListener() {
        binding.searchEditText.addTextChangedListener {
            viewModel.searchFieldValue(it.toString())
        }
    }

    private fun swipeToRefreshData() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.getSearchResult()
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun scrollToTop() {
        binding.scrollToTop.setOnClickListener {
            binding.searchRecycler.postDelayed({
                binding.searchRecycler.smoothScrollToPosition(0)
            }, 250)
        }
    }
}