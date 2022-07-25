package com.bronski.githubapiautodoc.userdetails.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bronski.githubapiautodoc.R
import com.bronski.githubapiautodoc.core.api.data.UserResponseResult
import com.bronski.githubapiautodoc.core.state.ViewState
import com.bronski.githubapiautodoc.core.ui.BaseFragment
import com.bronski.githubapiautodoc.databinding.FragmentUserDetailsBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsFragment : BaseFragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: UserDetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<UserDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.nickname != "") {
            viewModel.getUserDetails(args.nickname)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkViewState()
        swipeToRefreshData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkViewState() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect {
                when (it) {
                    is ViewState.SuccessUserState -> {
                        initDetailsScreen(it.userResponseResult)
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

    private fun swipeToRefreshData() {
        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.getUserDetails(args.nickname)
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    private fun initDetailsScreen(userResponseResult: UserResponseResult) {
        with(binding) {
            nicknameTextView.text = userResponseResult.login
            bioTextView.text = userResponseResult.bio
            followerTextView.text = userResponseResult.followers.toString()
            followingTextView.text = userResponseResult.following.toString()
            siteTextView.text = "Website: " + userResponseResult.webSite
            getTwitterName(this, userResponseResult)
            loadImage(this, userResponseResult)

        }
    }

    private fun loadImage(
        binding: FragmentUserDetailsBinding,
        userResponseResult: UserResponseResult
    ) {
        Glide.with(binding.avatarImageView.context)
            .load(userResponseResult.avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.ic_avatar_circle_24)
            .error(R.drawable.ic_avatar_circle_24)
            .into(binding.avatarImageView)
    }

    private fun getTwitterName(
        binding: FragmentUserDetailsBinding,
        userResponseResult: UserResponseResult
    ) =
        if (userResponseResult.twitterUsername != null) {
            binding.socialNetworkTextView.text = "Twitter: @" + userResponseResult.twitterUsername
        } else {
            binding.socialNetworkTextView.text = "Coming soon"
        }
}