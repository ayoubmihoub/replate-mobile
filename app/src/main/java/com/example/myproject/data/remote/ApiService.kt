package com.example.myproject.data.remote


import androidx.compose.ui.graphics.vector.Path
import com.example.myproject.data.model.AuthRequest
import com.example.myproject.data.model.AuthResponse
import com.example.myproject.data.model.MessageResponse
import com.example.myproject.data.model.RegisterRequest
import com.example.myproject.data.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // LOGIN
    @POST("users/login")
    fun login(@Body request: AuthRequest): Call<AuthResponse>

    // REGISTER
    @POST("users/register")
    fun register(@Body request: RegisterRequest): Call<MessageResponse>

    // ADMIN — validate account
    @POST("admin/validate/{id}")
    fun validateAccount(@Path("id") id: Long): Call<String>

    // ADMIN — get pending accounts
    @GET("admin/pending")
    fun getPendingAccounts(): Call<List<User>>
}
