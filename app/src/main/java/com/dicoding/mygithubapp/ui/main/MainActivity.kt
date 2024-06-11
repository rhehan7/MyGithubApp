package com.dicoding.mygithubapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.data.remote.response.UsersGithubItem
import com.dicoding.mygithubapp.databinding.ActivityMainBinding
import com.dicoding.mygithubapp.ui.ListUserAdapter
import com.dicoding.mygithubapp.ui.favorite.FavoriteActivity
import com.dicoding.mygithubapp.ui.setting.SettingActivity
import com.dicoding.mygithubapp.ui.setting.SettingPreferences
import com.dicoding.mygithubapp.ui.setting.SettingViewModel
import com.dicoding.mygithubapp.ui.setting.SettingViewModelFactory
import com.dicoding.mygithubapp.ui.setting.dataStore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // handle splash screen
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // set screen on recycleview
        setUpRecyclerView()
        // observe ViewModel
        observeViewModel()
        // set data user when search
        setupSearch()
        // show item menu in search bar
        showItemMenu()
        // get theme setting
        getThemeSetting()
    }

    private fun getThemeSetting() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }


    private fun showItemMenu() {
        with(binding) {
            searchBar.inflateMenu(R.menu.item_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.fav_menu -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    R.id.settings_menu -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setupSearch() {
        // implement searchview & searchbar
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                        val query = searchView.editText.text.toString().trim()
                        if (query.isEmpty()) {
                            Toast.makeText(
                                this@MainActivity,
                                "Inputan pencarian tidak boleh kosong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            mainViewModel.findUser(query)
                            searchBar.setText(searchView.text)
                            searchView.hide()
                        }
                        true
                    } else {
                        false
                    }
                }
        }
    }

    private fun observeViewModel() {
        // observe progressbar
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        // observe list user
        mainViewModel.listUser.observe(this) { userGithub ->
            setUserData(userGithub)
        }
        // observe message
        mainViewModel.messageEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        // inisialisation divider to each item on recycler view
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        with(binding) {
            rvUserGithub.layoutManager = layoutManager
            rvUserGithub.setHasFixedSize(true)
            rvUserGithub.addItemDecoration(itemDecoration) // add divider
        }
    }

    private fun setUserData(userGithub: List<UsersGithubItem>) {
        val adapter = ListUserAdapter()
        adapter.submitList(userGithub)
        binding.rvUserGithub.adapter = adapter

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}