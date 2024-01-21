package com.example.biletzone.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.biletzone.databinding.ActivityProfileBinding
import com.example.biletzone.models.Profile

class ProfileActivity : AppCompatActivity() {
    private var binding: ActivityProfileBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Retrieve the profile data from the intent
        val profile: Profile? = intent.getParcelableExtra(PROFILE_KEY)

        // Update the TextView elements with the profile data
        profile?.let {
            binding?.textId?.text = "ID: ${it.id}"
            binding?.textFirstName?.text = "First Name: ${it.firstName ?: "N/A"}"
            binding?.textLastName?.text = "Last Name: ${it.lastName ?: "N/A"}"
            binding?.textPhoneNumber?.text = "Phone Number: ${it.phoneNumber ?: "N/A"}"
            binding?.textUserUUID?.text = "User UUID: ${it.userUUID ?: "N/A"}"
        }
    }

    companion object {
        const val PROFILE_KEY = "profile"
    }
}
