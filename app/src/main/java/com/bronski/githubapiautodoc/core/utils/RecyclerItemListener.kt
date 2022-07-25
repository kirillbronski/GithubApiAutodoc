package com.bronski.githubapiautodoc.core.utils

import com.bronski.githubapiautodoc.core.api.data.SearchResponseResult

interface RecyclerItemListener {
    fun onItemClick(itemRepository: SearchResponseResult)
}