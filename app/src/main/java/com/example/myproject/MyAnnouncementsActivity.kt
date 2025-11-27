package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.model.Announcement
import com.example.myproject.data.remote.ApiService
import com.example.myproject.data.remote.NetworkResult
import com.example.myproject.data.remote.RetrofitClient
import com.example.myproject.data.repository.AnnouncementRepository
import com.example.myproject.data.session.SessionManager
import com.example.myproject.ui.AnnouncementViewModel
import com.example.myproject.ui.AnnouncementViewModelFactory

class MyAnnouncementsActivity : AppCompatActivity() {

    private lateinit var viewModel: AnnouncementViewModel

    private lateinit var tvAnnouncementsCount: TextView
    private lateinit var tvMyAnnouncementsCount: TextView
    private lateinit var layoutEmptyState: LinearLayout
    // NOTE: C'est ici que vous déclareriez votre RecyclerView et son Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_announcments)

        // 1. Initialisation des Vues
        tvAnnouncementsCount = findViewById(R.id.tv_announcements_count)
        tvMyAnnouncementsCount = findViewById(R.id.tv_my_announcements_count)
        layoutEmptyState = findViewById(R.id.layout_empty_state)

        // --- INITIALISATION MVVM (CORRIGÉE) ---
        // Utilisation directe de RetrofitClient.api qui est déjà une instance d'ApiService
        val apiService = RetrofitClient.api
        val repository = AnnouncementRepository(apiService, SessionManager)
        val factory = AnnouncementViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AnnouncementViewModel::class.java]
        // --------------------------------------

        // 2. LOGIQUE UI (navigation, boutons, etc.)
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }
        findViewById<CardView>(R.id.btn_new_announcement).setOnClickListener { navigateToNewAnnouncement() }
        findViewById<Button>(R.id.btn_make_announcement_empty).setOnClickListener { navigateToNewAnnouncement() }

        // 3. OBSERVATION MVVM
        observeAnnouncements()

        // Chargement initial des données
        viewModel.fetchMyAnnouncements()
    }

    override fun onResume() {
        super.onResume()
        // Recharger la liste chaque fois que l'activité est affichée
        viewModel.fetchMyAnnouncements()
    }

    /**
     * Gère l'observation du LiveData des annonces.
     */
    private fun observeAnnouncements() {
        // Observation du statut de chargement
        viewModel.isLoading.observe(this) { isLoading ->
            // Ici, vous pouvez afficher/cacher un indicateur de chargement global si nécessaire
        }

        // Observation du résultat
        viewModel.announcements.observe(this) { result ->
            result?.let {
                when (it) {
                    is NetworkResult.Success -> {
                        val announcements = it.data ?: emptyList()
                        updateUI(announcements)
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        updateUI(emptyList()) // Affiche 0 et l'état vide en cas d'erreur
                    }
                }
            }
        }
    }

    private fun updateUI(announcements: List<Announcement>) {
        val count = announcements.size

        // Mise à jour des compteurs
        tvAnnouncementsCount.text = count.toString()
        tvMyAnnouncementsCount.text = count.toString()

        // Gérer l'affichage de l'état vide
        if (count == 0) {
            layoutEmptyState.visibility = View.VISIBLE
        } else {
            layoutEmptyState.visibility = View.GONE
            // Logique pour afficher la liste réelle dans une RecyclerView...
        }
    }

    private fun navigateToNewAnnouncement() {
        val intent = Intent(this, NewAnnouncementActivity::class.java)
        startActivity(intent)
    }
}