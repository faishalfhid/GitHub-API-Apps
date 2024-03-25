package com.dicoding.sumbmissionawal.data.retrofit

import com.dicoding.sumbmissionawal.BuildConfig
import com.dicoding.sumbmissionawal.data.response.DetailUserResponse
import com.dicoding.sumbmissionawal.data.response.GithubResponse
import com.dicoding.sumbmissionawal.data.response.ItemsItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers;
import retrofit2.http.GET;

interface ApiService {
    @Headers(BuildConfig.AUTH)
    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>

    object GitHubApiClient {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}

