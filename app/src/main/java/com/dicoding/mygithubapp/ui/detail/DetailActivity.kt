package com.dicoding.mygithubapp.ui.detail

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.data.local.entity.FavoriteUser
import com.dicoding.mygithubapp.data.remote.response.DetailUserResponse
import com.dicoding.mygithubapp.databinding.ActivityDetailBinding
import com.dicoding.mygithubapp.ui.ListUserAdapter.Companion.EXTRA_AVATAR_URL
import com.dicoding.mygithubapp.ui.ListUserAdapter.Companion.EXTRA_USERNAME
import com.dicoding.mygithubapp.ui.SectionsPagerAdapter
import com.dicoding.mygithubapp.ui.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    private lateinit var detailFavoriteViewModel: DetailFavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)

        // Set initial data from Intent
        with(binding) {
            Glide.with(root.context)
                .load(avatarUrl)
                .circleCrop()
                .into(imgUserPictureDetail)
            tvUsernameDetail.text = username
        }

        if (username != null) {
            detailViewModel.getUserDetail(username)
        }

        detailFavoriteViewModel = obtainViewModel(this@DetailActivity)
        // Observe ViewModel for user detail
        observeViewModel()
        // Setup SectionsPagerAdapter and TabLayoutMediator
        setupViewPagerAndTabs(username)
        // setting status bar & navbar color
        setStatusAndNavBarColor()
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


    private fun obtainViewModel(activity: AppCompatActivity): DetailFavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailFavoriteViewModel::class.java]
    }

    private fun observeViewModel() {
        detailViewModel.userDetail.observe(this) { userDetail ->
            setUserDetailData(userDetail)
            handleFavoriteFAB(userDetail)
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setUserDetailData(userDetail: DetailUserResponse) {
        with(binding) {
            tvNameUser.text = userDetail.name?.toString()
            tvFollowers.text = this@DetailActivity.resources.getString(
                R.string.followers_text,
                userDetail.followers
            )
            tvFollowing.text = this@DetailActivity.resources.getString(
                R.string.following_text,
                userDetail.following
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupViewPagerAndTabs(username: String?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this).apply {
            this.username = username ?: ""
        }
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(tabTitles[position])
        }.attach()
    }

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )

    private fun handleFavoriteFAB(userData: DetailUserResponse) {
        val favoriteUser = FavoriteUser(username = userData.login, avatarUrl = userData.avatarUrl)

        detailFavoriteViewModel.getFavoriteByUsername(userData.login).observe(this) { userFav ->
            val isAvailable: Boolean = (userFav != null)

            if (isAvailable) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                binding.fabFavorite.setOnClickListener {
                    detailFavoriteViewModel.deleteFavorite(favoriteUser)
                }
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_unfavorite)
                binding.fabFavorite.setOnClickListener {
                    detailFavoriteViewModel.insertFavorite(favoriteUser)
                }
            }
        }
    }
}
