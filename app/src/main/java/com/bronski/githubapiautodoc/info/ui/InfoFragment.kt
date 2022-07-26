package com.bronski.githubapiautodoc.info.ui

import com.bronski.githubapiautodoc.core.ui.BaseFragment
import com.bronski.githubapiautodoc.databinding.FragmentInfoBinding

class InfoFragment : BaseFragment<FragmentInfoBinding>() {
    override fun getViewBinding() = FragmentInfoBinding.inflate(layoutInflater)
}