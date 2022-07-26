package com.bronski.githubapiautodoc.core.utils

import com.bronski.githubapiautodoc.core.api.data.Repository

interface RecyclerItemListener {
    fun onItemClick(itemRepository: Repository)
}