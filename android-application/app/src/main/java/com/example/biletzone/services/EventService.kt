package com.example.biletzone.services

import android.app.Activity
import com.example.biletzone.activities.MainActivity
import com.example.biletzone.models.Event
import com.example.biletzone.retrofit.RetrofitClient
import com.example.biletzone.utils.Constants
import com.example.biletzone.utils.Constants.eventList
import com.example.biletzone.utils.Utility.showToast

class EventService {
    private val retrofit = RetrofitClient.instance

    fun loadEventsData(activity: MainActivity, readEventsFlag: Boolean = false) {
        activity.updateNavigationUserDetails(Constants.userProfile, readEventsFlag)
        // Get user through Retrofit
//        val jwt = activity.getJwtTokenFromSharedPreferences()
//        val userUUID = jwt?.let { getSubjectFromJwt(it) }
//
//        if (userUUID != null) {
//            val call: Call<CustomResponse<Profile>> = retrofit.getUserProfile(userUUID)
//
//            call.enqueue(object : Callback<CustomResponse<Profile>> {
//                override fun onResponse(
//                    call: Call<CustomResponse<Profile>>,
//                    response: Response<CustomResponse<Profile>>
//                ) {
//                    if (response.isSuccessful) {
//                        val profileResponse: CustomResponse<Profile>? = response.body()
//                        val profile: Profile? = profileResponse?.payload
//
//                        if (profile != null) {
//                            // User profile retrieved successfully
//                            activity.updateNavigationUserDetails(profile, readEventsFlag)
//                            showToast(activity, "User profile loaded successfully")
//                        } else {
//                            // Handle the case where the profile data is null
//                            // This may indicate an issue with the server response
//                            showToast(activity, "Profile data is null")
//                        }
//                    } else {
//                        // Handle the case where the HTTP request was not successful
//                        // This may include handling different HTTP status codes
//                        showToast(activity, "HTTP request was not successful. Status code: ${response.code()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<CustomResponse<Profile>>, t: Throwable) {
//                    // Handle network failures or other errors
//                    // This may include displaying an error message to the user
//                    t.printStackTrace()
//                    showToast(activity, "Network request failed: ${t.message}")
//                }
//            })
//        } else {
//            // Handle the case where userUUID is null
//            // This may indicate an issue with retrieving the JWT token or decoding it
//            showToast(activity, "User UUID is null")
//        }
    }

    fun getEvents(activity: MainActivity) {
//        val call: Call<CustomResponse<List<Event>>> = retrofit.getEvents()
//
//        call.enqueue(object : Callback<CustomResponse<List<Event>>> {
//            override fun onResponse(
//                call: Call<CustomResponse<List<Event>>>,
//                response: Response<CustomResponse<List<Event>>>
//            ) {
//                if (response.isSuccessful) {
//                    val eventsResponse: CustomResponse<List<Event>>? = response.body()
//                    val events: List<Event>? = eventsResponse?.payload
//
//                    if (events != null) {
//                        // Events retrieved successfully
//                        activity.populateEventListInUI(ArrayList(events))
//                        showToast(activity, "Events loaded successfully")
//                    } else {
//                        // Handle the case where the events list is null
//                        showToast(activity, "Events list is null")
//                    }
//                } else {
//                    // Handle the case where the HTTP request was not successful
//                    // This may include handling different HTTP status codes
//                    showToast(activity, "HTTP request was not successful. Status code: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<CustomResponse<List<Event>>>, t: Throwable) {
//                // Handle network failures or other errors
//                // This may include displaying an error message to the user
//                t.printStackTrace()
//                showToast(activity, "Network request failed: ${t.message}")
//            }
//        })
        activity.populateEventListInUI(ArrayList(eventList))
    }

    fun addEvent(activity: Activity, event: Event) {
        showToast(activity, "Event added successfully!")

//        val call = retrofit.addEvent(event)
//
//        call.enqueue(object : Callback<CustomResponse<Void>> {
//            override fun onResponse(call: Call<CustomResponse<Void>>, response: Response<CustomResponse<Void>>) {
//                if (response.isSuccessful) {
//                    val customResponse = response.body()
//
//                    if (customResponse != null) {
//                        // Check the status or other fields in customResponse if needed
//                        if (customResponse.status == "success") {
//                            // Handle successful response
//                            // For example, show a success message or navigate to another activity
//                            showToast("Event added successfully")
//                            finish() // Optionally, finish the activity after adding an event
//                        } else {
//                            // Handle unsuccessful response with specific status
//                            showToast("Failed to add event: ${customResponse.message}")
//                        }
//                    } else {
//                        // Handle null body in response
//                        showToast("Response body is null")
//                    }
//                } else {
//                    // Handle error response
//                    // For example, show an error message to the user
//                    showToast("Failed to add event. Error: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<CustomResponse<Void>>, t: Throwable) {
//                // Handle failure
//                // For example, show an error message to the user
//                showToast("Failed to add event. Check your internet connection.")
//            }
//        })
    }
}