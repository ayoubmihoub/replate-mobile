package com.example.myproject.data.remote

import com.example.myproject.data.model.Announcement
import com.example.myproject.data.model.AnnouncementRequest
import com.example.myproject.data.model.AuthRequest
import com.example.myproject.data.model.AuthResponse
import com.example.myproject.data.model.MessageResponse
import com.example.myproject.data.model.RegisterRequest
import com.example.myproject.data.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // LOGIN
    @POST("users/login")
    fun login(@Body request: AuthRequest): Call<AuthResponse>

    // REGISTER
    @POST("users/register")
    fun register(@Body request: RegisterRequest): Call<MessageResponse>

    // ADMIN — Pending Accounts (Route restaurée)
    @GET("admin/pending")
    fun getPendingAccounts(
        @Header("Authorization") token: String
    ): Call<List<User>>

    // ADMIN — validate account
    @POST("admin/validate/{id}")
    fun validateAccount(
        @Path("id") userId: Long,
        @Header("Authorization") token: String
    ): Call<ResponseBody>

    // OFFERS MANAGEMENT SERVICE

    // MERCHANT - Créer une annonce (POST /offers/create)
    @POST("offers/create")
    fun createAnnouncement(
        @Body request: AnnouncementRequest,
        @Header("X-User-Id") userId: Long,
        @Header("X-User-Role") userRole: String,
        @Header("X-Is-Validated") isValidated: Boolean
    ): Call<Announcement>

    // MERCHANT - Lister mes annonces (GET /offers/my-offers)
    @GET("offers/my-offers")
    fun getMyAnnouncements(
        @Header("X-User-Id") userId: Long,
        @Header("X-User-Role") userRole: String
    ): Call<List<Announcement>>
}