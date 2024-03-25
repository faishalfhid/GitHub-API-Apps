package com.dicoding.sumbmissionawal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sumbmissionawal.data.response.ItemsItem
import com.dicoding.sumbmissionawal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var profileAdapter: ProfileAdapter

    private val defaultQueryKey = "DEFAULT_QUERY"
    private var defaultQuery: String = "faishalfhid"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            mainViewModel.fetchProfiles(defaultQuery)
        }

            with(binding) {
                searchView.requestFocus()
                searchView.setupWithSearchBar(searchBar)
                searchView
                    .editText
                    .setOnEditorActionListener { textView, actionId, event ->
                        val query = searchView.text.toString()
                        if (query.isNotEmpty()){
                            mainViewModel.fetchProfiles(query)
                            searchBar.setText(searchView.text)
                        } else {
                            Toast.makeText(this@MainActivity, "Mohon masukkan username", Toast.LENGTH_SHORT).show()
                        }
                        searchView.hide()
                        false
                    }
            }

        savedInstanceState?.getString(defaultQueryKey)?.let { savedQuery ->
            defaultQuery = savedQuery
        }

        setupRecyclerView()

        observeViewModel()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(defaultQueryKey, defaultQuery)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.profiles.value?.let { setProfileData(it) }
    }

    private fun showNoResultsMessage() {
        Toast.makeText(this@MainActivity, "Username yang anda cari tidak ada", Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProfiles.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewProfiles.addItemDecoration(itemDecoration)

        profileAdapter = ProfileAdapter()
        binding.recyclerViewProfiles.adapter = profileAdapter
    }

    private fun observeViewModel() {
        mainViewModel.loading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        mainViewModel.profiles.observe(this) { profiles ->
            setProfileData(profiles)
        }
    }

    private fun setProfileData(profileData: List<ItemsItem>) {
        if (profileData.isEmpty()) {
            showNoResultsMessage()
        } else {
            profileAdapter.setData(profileData)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}





