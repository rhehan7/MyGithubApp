package com.dicoding.mygithubapp.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.data.remote.response.UsersGithubItem
import com.dicoding.mygithubapp.databinding.FragmentFollowBinding
import com.dicoding.mygithubapp.ui.ListUserAdapter

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME) ?: ""

            if (position == 1) {
                followViewModel.getFollowers(username)
            } else {
                followViewModel.getFollowing(username)
            }
        }
        // set screen on recycleview
        setUpRecyclerView()
        // observe ViewModel
        observeViewModel()
    }

    private fun observeViewModel() {
        // observe progressbar
        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        // observe list users
        followViewModel.listUsers.observe(viewLifecycleOwner) { users ->
            setUserData(users)
        }
        // observe message
        followViewModel.messageEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUserGithub.layoutManager = layoutManager
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

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}