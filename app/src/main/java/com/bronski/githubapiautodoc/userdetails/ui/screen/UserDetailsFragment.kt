package com.bronski.githubapiautodoc.userdetails.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bronski.githubapiautodoc.R
import com.bronski.githubapiautodoc.core.api.data.User
import com.bronski.githubapiautodoc.core.state.ViewState
import com.bronski.githubapiautodoc.core.ui.BaseFragment
import com.bronski.githubapiautodoc.databinding.FragmentUserDetailsBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {

    private val args: UserDetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<UserDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.nickname != "") {
            viewModel.getUserDetails(args.nickname)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkViewState()
        swipeToRefreshData()
    }

    private fun checkViewState() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.observe(viewLifecycleOwner) {
                when (it) {
                    is ViewState.SuccessState -> {
                        initDetailsScreen(it.user)
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

    private fun initDetailsScreen(userResponseResult: User) {
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
        user: User,
    ) {
        Glide.with(binding.avatarImageView.context)
            .load(user.avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.ic_avatar_circle_24)
            .error(R.drawable.ic_avatar_circle_24)
            .into(binding.avatarImageView)
    }

    private fun getTwitterName(
        binding: FragmentUserDetailsBinding,
        user: User,
    ) =
        if (user.twitterUsername != null) {
            binding.socialNetworkTextView.text = "Twitter: @" + user.twitterUsername
        } else {
            binding.socialNetworkTextView.text = "Coming soon"
        }

    override fun getViewBinding() = FragmentUserDetailsBinding.inflate(layoutInflater)
}