package com.dicoding.sumbmissionawal.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.sumbmissionawal.R
import com.dicoding.sumbmissionawal.data.response.DetailUserResponse
import com.dicoding.sumbmissionawal.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailViewModel
    private val defaultQueryKey = "DEFAULT_USERNAME"
    private var defaultUsername: String = ""
    private var isDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        savedInstanceState?.getString(defaultQueryKey)?.let { savedUsername ->
            defaultUsername = savedUsername
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)

        if (username != null && defaultUsername != username && !isDataLoaded) {
            viewModel.fetchUserDetail(username)
        }

        val sectionPagerAdapter = SectionsPagerAdapter(this)
        sectionPagerAdapter.username = username ?: ""
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.userDetail.observe(this) { userDetail ->
            showUserDetail(userDetail)
        }
    }

    private fun showUserDetail(userDetail: DetailUserResponse) {
        Picasso.get().load(userDetail.avatarUrl).into(binding.imageView)
        binding.tvUsername.text = userDetail.login
        binding.tvNama.text = userDetail.name
        binding.tvJumlahFollowers.text = "${userDetail.followers} Followers"
        binding.tvJumlahFollowing.text = "${userDetail.following} Following"
        isDataLoaded = true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}

