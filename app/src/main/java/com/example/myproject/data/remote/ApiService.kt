package com.example.myproject.data.remote


import androidx.compose.ui.graphics.vector.Path
import com.example.myproject.data.model.Announcement // Importé pour la création
import com.example.myproject.data.model.AnnouncementRequest // Importé pour la création
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

    // -----------------------------------------------------------------------
    // ANNOUNCEMENT / OFFER ENDPOINTS (CORRIGÉS POUR LE 401)
    // -----------------------------------------------------------------------

    // CRÉATION D'ANNONCE
    @POST("offers/create") // Assumant l'endpoint de création du backend
    fun createAnnouncement(
        @Body request: AnnouncementRequest,
        @Header("Authorization") token: String // <-- Résout l'erreur 401
    ): Call<Announcement>

    // RÉCUPÉRATION DES ANNONCES DE L'UTILISATEUR
    @GET("offers/my-offers") // Assumant l'endpoint de récupération du backend
    fun getMyAnnouncements(
        @Header("Authorization") token: String // <-- Résout l'erreur 401
    ): Call<List<Announcement>>


    // -----------------------------------------------------------------------
    // ADMIN ENDPOINTS
    // -----------------------------------------------------------------------

    // ADMIN — validate account
    @GET("admin/pending")
    fun getPendingAccounts(
        @Header("Authorization") token: String
    ): Call<List<User>>

    @POST("admin/validate/{id}")
    fun validateAccount(
        @Path("id") userId: Long,
        @Header("Authorization") token: String
    ): Call<ResponseBody>
}