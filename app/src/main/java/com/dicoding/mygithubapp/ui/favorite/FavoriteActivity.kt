package com.dicoding.mygithubapp.ui.favorite

import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.data.local.entity.FavoriteUser
import com.dicoding.mygithubapp.data.remote.response.UsersGithubItem
import com.dicoding.mygithubapp.databinding.ActivityFavoriteBinding
import com.dicoding.mygithubapp.ui.ListUserAdapter
import com.dicoding.mygithubapp.ui.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        // set screen on recycler view
        setUpRecyclerView()
        // observe fav model
        observeViewModel()
        // setting status bar & navbar color
        setStatusAndNavBarColor()
        // BackButton
        binding.toAppBar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setStatusAndNavBarColor() {
        val typedValue = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true)
        val color = typedValue.data

        val typedValue2 = TypedValue()
        theme.resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValue, true)
        val color2 = typedValue2.data

        window.statusBarColor = color
        window.navigationBarColor = color2
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.layoutManager = layoutManager
        // add divider to each item on recycler view
        // val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        // binding.rvFavUser.addItemDecoration(itemDecoration)
        binding.rvFavUser.setHasFixedSize(true)
    }

    private fun observeViewModel() {
        favoriteViewModel.getFavoriteUser().observe(this) { users ->
            setFavoriteUsers(users)
        }
    }

    private fun setFavoriteUsers(user: List<FavoriteUser>?) {
        val listItems = ArrayList<UsersGithubItem>()
        user?.let { favoriteUsers ->
            listItems.addAll(favoriteUsers.map { favoriteUser ->
                UsersGithubItem(favoriteUser.avatarUrl ?: "", favoriteUser.username)
            })
        }

        if (user == null || user == ArrayList<UsersGithubItem>()) {
            binding.textInfo.text = getString(R.string.favorite_user_empty)
        }

        val adapter = ListUserAdapter()
        adapter.submitList(listItems)
        binding.rvFavUser.adapter = adapter
    }
}