package com.example.myproject.data.ui.login // ou .ui.login selon votre préférence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.repository.LoginRepository
import com.example.myproject.ui.login.LoginModelView
import java.lang.IllegalArgumentException

/**
 * Factory personnalisée pour instancier les ViewModels avec des dépendances.
 */
class ViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginModelView::class.java)) {
            // Ici, on injecte le LoginRepository dans le LoginModelView
            return LoginModelView(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}