package com.example.biletzone.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.biletzone.R
import com.example.biletzone.databinding.ActivitySignInBinding
import com.example.biletzone.models.requests.LoginData
import com.example.biletzone.models.responses.CustomResponse
import com.example.biletzone.retrofit.RetrofitClient
import com.example.biletzone.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.KeyStore
import javax.crypto.KeyGenerator

class SignInActivity : BaseActivity() {
    private var binding: ActivitySignInBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
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

        binding?.btnSignIn?.setOnClickListener {
            // hide the keyboard after btn pressed
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(binding?.llSigninFields?.windowToken, 0)

            signInRegisteredUser()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding?.toolbarSignInActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_arrow_back_24dp)
        }

        binding?.toolbarSignInActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun signInRegisteredUser() {
        val gmail: String = binding?.etGmail?.text.toString().trim()
        val password: String = binding?.etPassword?.text.toString().trim()

        if (validateForm(gmail, password)) {
            val loginData = LoginData(gmail, password)
            val retrofit = RetrofitClient.instance

            showProgressDialog(resources.getString(R.string.please_wait))

            val loginCall = retrofit.loginUser(loginData)
            loginCall.enqueue(object : Callback<CustomResponse<String>> {
                override fun onResponse(call: Call<CustomResponse<String>>, response: Response<CustomResponse<String>>) {
                    if (response.isSuccessful) {
                        val customResponse = response.body()

                        // Log the customResponse after a successful response
                        Log.d("SignInActivity", "Successful Login Response: $customResponse")

                        // Handle successful registration response
                        if (customResponse != null) {
                            if (customResponse.message != null && customResponse.payload != null) {
                                signInSuccess(customResponse.message, customResponse.payload)
                            }
                        }
                    } else {
                        // Log the error response
                        Log.e("SignInActivity", "Error Response: ${response.errorBody()?.string()}")

                        // Handle error response
                        showToast(this@SignInActivity, "Login failed")
                    }
                    hideProgressDialog()
                }

                override fun onFailure(call: Call<CustomResponse<String>>, t: Throwable) {
                    // Log the failure
                    Log.e("SignInActivity", "Login Failure: ${t.message}", t)

                    // Handle failure
                    showToast(this@SignInActivity, "Login failed. Please try again.")
                    hideProgressDialog()
                }
            })

//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    hideProgressDialog()
//                    if (task.isSuccessful) {
//                        // Sign in success
//                        FirestoreClass().loadUserData(this)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w("Sign In", "signInWithEmail:failure", task.exception)
//                        Toast.makeText(
//                            baseContext,
//                            "Authentication failed. ${task.exception?.message}",
//                            Toast.LENGTH_SHORT,
//                        ).show()
//                    }
//                }
        }
    }

    private fun validateForm(gmail: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(gmail) -> {
                showErrorSnackBar("Please enter an email...")
                false
            }

            !isValidEmail(gmail) -> {
                showErrorSnackBar("Please enter a valid email address...")
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

    fun signInSuccess(message: String, jwt: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
        saveJwtTokenToSharedPreferences(jwt)
        hideProgressDialog()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    private fun saveJwtTokenToSharedPreferences(jwtToken: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        // Save the JWT token to SharedPreferences
        editor.putString(Constants.JWT_SHARED_PREF_NAME, jwtToken)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}