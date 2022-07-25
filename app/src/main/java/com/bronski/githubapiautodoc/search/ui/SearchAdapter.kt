package com.bronski.githubapiautodoc.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bronski.githubapiautodoc.R
import com.bronski.githubapiautodoc.core.api.data.SearchResponseResult
import com.bronski.githubapiautodoc.core.utils.RecyclerItemListener
import com.bronski.githubapiautodoc.databinding.ItemRepositoryBinding
import com.bumptech.glide.Glide

class SearchAdapter(
    private val listener: RecyclerItemListener
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val repositoryListDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<SearchResponseResult>) = repositoryListDiffer.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepositoryBinding.inflate(inflater, parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val repository = repositoryListDiffer.currentList[position]
        holder.bind(repository)
    }

    override fun getItemCount(): Int = repositoryListDiffer.currentList.size

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchResponseResult>() {
            override fun areItemsTheSame(
                oldItem: SearchResponseResult,
                newItem: SearchResponseResult
            ): Boolean {
                return oldItem.nodeId == newItem.nodeId
            }

            override fun areContentsTheSame(
                oldItem: SearchResponseResult,
                newItem: SearchResponseResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class SearchViewHolder(
        val binding: ItemRepositoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemRepository: SearchResponseResult) = with(binding) {
            titleTextView.text = itemRepository.name
            languageTextView.text = itemRepository.language
            starsTextView.text = itemRepository.stargazersCount.toString()
            dataTextView.text = itemRepository.getUpdatedDateAsString()
            descriptionTextView.text = itemRepository.description
            Glide.with(photoImageView.context)
                .load(itemRepository.owner.avatarUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_avatar_circle_24)
                .error(R.drawable.ic_avatar_circle_24)
                .into(photoImageView)
            itemView.setOnClickListener {
                listener.onItemClick(itemRepository)
            }
        }
    }
}