package com.dicoding.sumbmissionawal.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sumbmissionawal.data.response.ItemsItem
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.sumbmissionawal.databinding.FragmentFollowersFollowingBinding

class FollowersFollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowersFollowingBinding
    private lateinit var viewModel: DetailViewModel

    private var position: Int = 0
    private var username: String = ""

    private var isLoadingCompleted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        savedInstanceState?.let {
            isLoadingCompleted = it.getBoolean(KEY_IS_LOADING_COMPLETED, false)
        }

        if (!isLoadingCompleted) {
            observeLoading()
        }

        arguments?.let {
            position = it.getInt(ARG_POSTION)
            username = it.getString(ARG_USERNAME)!!
        }

        if (position == 1) {
            observeFollowers()
        } else {
            observeFollowing()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LOADING_COMPLETED, isLoadingCompleted)
    }

    private fun observeLoading() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
            isLoadingCompleted = !isLoading
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun observeFollowers() {
        viewModel.getFollowers(username)
        viewModel.listFollower.observe(viewLifecycleOwner) { followers ->
            showFollower(followers)
        }
    }

    private fun showFollower(followers: List<ItemsItem>?) {
        if (followers.isNullOrEmpty()) {
            binding.recyclerViewProfiles.visibility = View.GONE
            binding.tvNoData.visibility = View.VISIBLE
            binding.tvNoData.text = "User ini tidak memiliki followers."
        } else {
            binding.recyclerViewProfiles.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.GONE
            binding.recyclerViewProfiles.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = ProfileAdapter()
            binding.recyclerViewProfiles.adapter = adapter
            followers?.let {
                adapter.setData(it)
            }
        }
    }

    private fun observeFollowing() {
        viewModel.getFollowing(username)
        viewModel.listFollowing.observe(viewLifecycleOwner) { following ->
            showFollowing(following)
        }
    }

    private fun showFollowing(following: List<ItemsItem>?) {
        if (following.isNullOrEmpty()) {
            binding.recyclerViewProfiles.visibility = View.GONE
            binding.tvNoData.visibility = View.VISIBLE
            binding.tvNoData.text = "User ini tidak mengikuti siapapun."
        } else {
            binding.recyclerViewProfiles.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.GONE
            binding.recyclerViewProfiles.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = ProfileAdapter()
            binding.recyclerViewProfiles.adapter = adapter
            following?.let {
                adapter.setData(it)
            }
        }
    }

    companion object {
        const val ARG_POSTION = "section_number"
        const val ARG_USERNAME = "username"
        const val KEY_IS_LOADING_COMPLETED = "is_loading_completed"
    }
}

