package com.example.biletzone.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.biletzone.R
import com.example.biletzone.databinding.ActivitySignUpBinding
import com.example.biletzone.models.requests.RegisterData
import com.example.biletzone.models.responses.CustomResponse
import com.example.biletzone.retrofit.RetrofitClient
import com.example.biletzone.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {
    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        binding?.btnSignUp?.setOnClickListener {
            // hide the keyboard after btn pressed
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding?.llSignupFields?.windowToken, 0)

            registerUser()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarSignUpActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_arrow_back_24dp)
        }

        binding?.toolbarSignUpActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun registerUser() {
        val firstName: String = binding?.etFirstName?.text.toString().trim()
        val lastName: String = binding?.etLastName?.text.toString().trim()
        val gmail: String = binding?.etGmail?.text.toString().trim()
        val password: String = binding?.etPassword?.text.toString().trim()
        val phoneNumber: String = binding?.etPhoneNumber?.text.toString().trim()
        val receiveNotifications: Boolean = binding?.checkboxNotifications?.isChecked ?: false

        if (validateForm(firstName, lastName, gmail, phoneNumber, password)) {
            val registerData = RegisterData(firstName, lastName, gmail, phoneNumber, password, Constants.USER_ROLE, receiveNotifications)
            val retrofit = RetrofitClient.instance

            // Log the registerData before making the request
            Log.d("SignUpActivity", "Register Data: $registerData")
            showProgressDialog(resources.getString(R.string.please_wait))

            val registerCall = retrofit.registerUser(registerData)
            registerCall.enqueue(object : Callback<CustomResponse<Void>> {
                override fun onResponse(call: Call<CustomResponse<Void>>, response: Response<CustomResponse<Void>>) {
                    if (response.isSuccessful) {
                        val customResponse = response.body()

                        // Log the customResponse after a successful response
                        Log.d("SignUpActivity", "Successful Registration Response: $customResponse")

                        // Handle successful registration response
                        if (customResponse != null) {
                            customResponse.message?.let { userRegisteredSuccess(it) }
                        }
                    } else {
                        // Log the error response
                        Log.e("SignUpActivity", "Error Response: ${response.errorBody()?.string()}")

                        // Handle error response
                        showToast(this@SignUpActivity, "Registration failed")
                    }
                    hideProgressDialog()
                }

                override fun onFailure(call: Call<CustomResponse<Void>>, t: Throwable) {
                    // Log the failure
                    Log.e("SignUpActivity", "Registration Failure: ${t.message}", t)

                    // Handle failure
                    showToast(this@SignUpActivity, "Registration failed. Please try again.")
                    hideProgressDialog()
                }
            })
        }
    }


    private fun validateForm(firstName: String, lastName: String, gmail: String, phoneNumber: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(firstName) -> {
                showErrorSnackBar("Please enter a first name...")
                false
            }

            TextUtils.isEmpty(lastName) -> {
                showErrorSnackBar("Please enter a last name...")
                false
            }

            TextUtils.isEmpty(gmail) -> {
                showErrorSnackBar("Please enter an email...")
                false
            }

            !isValidEmail(gmail) -> {
                showErrorSnackBar("Please enter a valid email address...")
                false
            }

            TextUtils.isEmpty(phoneNumber) -> {
                showErrorSnackBar("Please enter a phone number...")
                false
            }

            !isValidPhoneNumber(phoneNumber) -> {
                showErrorSnackBar("Please enter a valid phone number...")
                false
            }

            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password...")
                false
            }

            else -> true
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex()) && email.endsWith("@gmail.com", ignoreCase = true)
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Simple validation: checks if the phone number contains only digits
        return phoneNumber.matches("\\d+".toRegex())
    }


    fun userRegisteredSuccess(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
        hideProgressDialog()
        startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}