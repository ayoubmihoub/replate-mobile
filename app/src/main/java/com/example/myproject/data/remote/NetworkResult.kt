package com.example.myproject.data.remote

/**
 * Classe scellée (sealed class) pour gérer les différents états d'une requête réseau:
 * Succès avec des données, Erreur avec un message, ou Chargement.
 */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    // L'état de chargement sera géré par un LiveData<Boolean> dans le ViewModel.
}