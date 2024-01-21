package com.example.biletzone.retrofit.interfaces

import com.example.biletzone.models.Event
import com.example.biletzone.models.Profile
import com.example.biletzone.models.requests.LoginData
import com.example.biletzone.models.requests.RegisterData
import com.example.biletzone.models.responses.CustomResponse
import com.example.biletzone.utils.Endpoints
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // User endpoints
    @POST(Endpoints.GATEWAY_REGISTER)
    fun registerUser(@Body registerData: RegisterData): Call<CustomResponse<Void>>

    @POST(Endpoints.GATEWAY_LOGIN)
    fun loginUser(@Body credentials: LoginData): Call<CustomResponse<String>>

    @GET(Endpoints.GATEWAY_GET_PROFILE)
    fun getUserProfile(userUUID: String): Call<CustomResponse<Profile>>

    @GET(Endpoints.GATEWAY_GET_EVENTS)
    fun getEvents(): Call<CustomResponse<List<Event>>>

    @POST(Endpoints.GATEWAY_ADD_EVENT)
    fun addEvent(event: Event): Call<CustomResponse<Void>>
}