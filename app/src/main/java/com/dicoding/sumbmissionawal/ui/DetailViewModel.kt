package com.dicoding.sumbmissionawal.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.sumbmissionawal.data.response.DetailUserResponse
import com.dicoding.sumbmissionawal.data.response.ItemsItem
import com.dicoding.sumbmissionawal.data.retrofit.ApiConfig
import com.dicoding.sumbmissionawal.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse>
        get() = _userDetail

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _listFollower = MutableLiveData<List<ItemsItem>>() //
    val listFollower: LiveData<List<ItemsItem>>
        get() = _listFollower

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>>
        get() = _listFollowing

    private var isDataLoaded = false

    companion object {
        private const val TAG = "DetailViewModel"
    }

    internal fun getFollowers(username: String) {
        if (!isDataLoaded) {
            _loading.value = true
        }
        val client = ApiConfig.getApiService().getFollowers(username)

        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollower.postValue(responseBody!!)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    internal fun getFollowing(username: String) {
        if (!isDataLoaded) {
            _loading.value = true
        }
        val client = ApiConfig.getApiService().getFollowing(username)

        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _loading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.postValue(responseBody!!)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun fetchUserDetail(username: String) {
        if (!isDataLoaded) {
            _loading.value = true
        }
        ApiService.GitHubApiClient.create().getDetailUser(username).enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                    isDataLoaded = true
                } else {
                    _error.value = "Failed to fetch user detail: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Network error: ${t.message}"
            }
        })
    }
}
