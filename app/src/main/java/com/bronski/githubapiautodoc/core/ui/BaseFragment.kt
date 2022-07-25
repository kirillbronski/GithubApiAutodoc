package com.bronski.githubapiautodoc.core.ui

import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected fun showProgressIndicator(progressBar: ProgressBar) {
        progressBar.isVisible = true
    }

    protected fun hideProgressIndicator(progressBar: ProgressBar) {
        progressBar.isVisible = false
    }

    protected fun displayToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}