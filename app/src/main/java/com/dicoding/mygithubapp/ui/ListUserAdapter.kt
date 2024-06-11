package com.dicoding.mygithubapp.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithubapp.data.remote.response.UsersGithubItem
import com.dicoding.mygithubapp.databinding.ItemUserBinding
import com.dicoding.mygithubapp.ui.detail.DetailActivity

class ListUserAdapter : ListAdapter<UsersGithubItem, ListUserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UsersGithubItem) {
            with(binding) {
                tvUserName.text = user.login

                Glide.with(imgUserPicture.context)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(binding.imgUserPicture)

                // add event listener to each item & send data
                root.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java).apply {
                        putExtra(EXTRA_USERNAME, user.login)
                        putExtra(EXTRA_AVATAR_URL, user.avatarUrl)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UsersGithubItem>() {
            override fun areItemsTheSame(
                oldItem: UsersGithubItem,
                newItem: UsersGithubItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UsersGithubItem,
                newItem: UsersGithubItem
            ): Boolean {
                return oldItem == newItem
            }
        }

        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_AVATAR_URL = "EXTRA_AVATAR_URL"
    }

}