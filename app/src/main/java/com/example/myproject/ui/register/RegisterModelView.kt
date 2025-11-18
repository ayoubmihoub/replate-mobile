package com.example.myproject.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myproject.data.model.RegisterRequest
import com.example.myproject.data.model.MessageResponse
import com.example.myproject.data.model.UserRole
import com.example.myproject.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registrationStatus = MutableLiveData<String>()
    val registrationStatus: LiveData<String> = _registrationStatus

    private val _messageResponse = MutableLiveData<MessageResponse?>()
    val messageResponse: LiveData<MessageResponse?> = _messageResponse

    fun registerUser(
        email: String,
        password: String,
        role: UserRole,
        username: String,
        location: String? = null,
        phoneNumber: String? = null,
        documentUrl: String? = null
    ) {
        _registrationStatus.value = "Inscription en cours..."
        _messageResponse.value = null

        val request = RegisterRequest(
            email = email,
            password = password,
            role = role,
            username = username,
            location = location,
            phoneNumber = phoneNumber,
            documentUrl = documentUrl
        )

        RetrofitClient.api.register(request).enqueue(object : Callback<MessageResponse> {

            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _messageResponse.value = body
                        _registrationStatus.value = "Inscription réussie : ${body.message}"
                    } else {
                        _registrationStatus.value = "Erreur: Réponse du serveur vide après succès."
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Erreur inconnue"
                    _registrationStatus.value = "Échec de l'inscription: ${response.code()} - $errorBody"
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {

            }
        })
    }
}
