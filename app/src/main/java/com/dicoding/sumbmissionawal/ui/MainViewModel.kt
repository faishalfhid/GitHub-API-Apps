package com.dicoding.sumbmissionawal.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sumbmissionawal.data.response.ItemsItem
import com.dicoding.sumbmissionawal.data.response.GithubResponse
import com.dicoding.sumbmissionawal.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _profiles = MutableLiveData<List<ItemsItem>>()
    val profiles: LiveData<List<ItemsItem>>
        get() = _profiles

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun fetchProfiles(query: String) {
        _loading.value = true
        ApiService.GitHubApiClient.create().searchUsers(query).enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    val profiles = response.body()?.items ?: emptyList()
                    _profiles.value = (profiles ?: emptyList()) as List<ItemsItem>?
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}
