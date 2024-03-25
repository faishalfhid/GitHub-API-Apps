package com.dicoding.sumbmissionawal.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.sumbmissionawal.data.response.ItemsItem
import com.dicoding.sumbmissionawal.databinding.UserProfileBinding
import com.squareup.picasso.Picasso

class ProfileAdapter : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    private var profileList: List<ItemsItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(profiles: List<ItemsItem>) {
        profileList = profiles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = UserProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val profile = profileList[position]
        holder.bind(profile)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    inner class ProfileViewHolder(private val binding: UserProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ItemsItem) {
            binding.textViewUsername.text = profile.login
            Picasso.get().load(profile.avatarUrl).into(binding.imageViewProfile)
            binding.root.setOnClickListener {
                val context = binding.root.context
                if (context != null) {
                    val intent = Intent(context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USERNAME, profile.login ?: "")
                    context.startActivity(intent)
                } else {
                    Log.e("ProfileViewHolder", "Context is null")
                }
            }
        }
    }
}