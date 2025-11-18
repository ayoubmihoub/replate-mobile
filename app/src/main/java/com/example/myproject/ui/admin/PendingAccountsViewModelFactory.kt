package com.example.myproject.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

/**
 * Factory personnalisée pour injecter le token JWT dans le PendingAccountsViewModel.
 */
class PendingAccountsViewModelFactory(private val token: String) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PendingAccountsViewModel::class.java)) {
            // Crée une instance de PendingAccountsViewModel en lui passant le token
            return PendingAccountsViewModel(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}