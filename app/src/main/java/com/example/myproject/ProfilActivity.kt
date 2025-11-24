package com.example.myproject

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.Intent // Import nécessaire pour la navigation

class ProfilActivity : AppCompatActivity() {

    // Déclaration des vues nécessaires
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var btnBackProfile: ImageButton
    private lateinit var btnMenuProfile: ImageButton
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var navViewProfile: NavigationView
    private lateinit var statAnnouncementsCount: TextView
    private lateinit var statOffersCount: TextView
    private lateinit var statCompletedOffersCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Remplacez R.layout.profile par le nom exact de votre fichier XML (ex: R.layout.activity_profil)
        setContentView(R.layout.profile)

        // 1. Initialisation des vues (findViewById)
        initializeViews()

        // 2. Configuration des données
        setupProfileData()

        // 3. Configuration des écouteurs de clics
        setupClickListeners()

        // 4. Configuration du menu de navigation
        setupNavigationView()
    }

    // --- 1. Initialisation des vues ---
    private fun initializeViews() {
        // Vues principales du layout
        drawerLayout = findViewById(R.id.drawer_layout_profile)
        navViewProfile = findViewById(R.id.nav_view_profile)

        // Vues de l'en-tête
        btnBackProfile = findViewById(R.id.btn_back_profile)
        btnMenuProfile = findViewById(R.id.btn_menu_profile)

        // Vues de l'utilisateur
        profileName = findViewById(R.id.profile_name)
        profileEmail = findViewById(R.id.profile_email)

        // Vues des statistiques
        statAnnouncementsCount = findViewById(R.id.stat_announcements_count)
        statOffersCount = findViewById(R.id.stat_offers_count)
        statCompletedOffersCount = findViewById(R.id.stat_completed_offers_count)
    }

    // --- 2. Configuration des données ---
    private fun setupProfileData() {
        // Informations de l'utilisateur
        profileName.text = "John Doe"
        profileEmail.text = "johnDoe@gmail.com"

        // Statistiques
        statAnnouncementsCount.text = "13"
        statOffersCount.text = "5"
        statCompletedOffersCount.text = "15"
    }

    // --- 3. Configuration des écouteurs de clics ---
    private fun setupClickListeners() {
        // Bouton de retour (Flèche gauche)
        btnBackProfile.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Bouton de menu (Carré jaune, à droite) - Ouvre le DrawerLayout
        btnMenuProfile.setOnClickListener {
            // Ouvre le tiroir de navigation (NavigationView) attaché au 'start'
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    // --- 4. Configuration du menu de navigation (MISE À JOUR) ---
    private fun setupNavigationView() {
        // Gère les clics sur les éléments du menu de navigation
        navViewProfile.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // ASSUMANT QUE R.id.nav_logout EST L'ID DE L'ÉLÉMENT DE MENU
                R.id.nav_logout -> {
                    performLogout()
                    // Nous n'avons pas besoin de closeDrawer ici car performLogout ferme l'activité
                    true
                }
                // Ajoutez vos autres IDs d'éléments de menu ici
                else -> {
                    // Fermer le tiroir pour tout autre élément non géré
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }
    }

    // --- NOUVELLE FONCTION : Gestion de la Déconnexion ---
    /**
     * Effectue la déconnexion en effaçant les données de session (SharedPreferences)
     * et en redirigeant vers l'écran de connexion (LoginActivity).
     */
    private fun performLogout() {
        // 1. Supprimer les données de session stockées
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().apply {
            clear() // Efface le TOKEN, les IDs d'utilisateur, etc.
            apply()
        }

        // 2. Rediriger vers l'écran de connexion
        val intent = Intent(this, LoginActivity::class.java).apply {
            // Ces flags garantissent que l'utilisateur ne peut pas revenir à ProfilActivity
            // en appuyant sur le bouton Retour.
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        // 3. Fermer ProfilActivity pour qu'elle ne soit plus dans la pile
        finish()
    }

    override fun onBackPressed() {
        // Gère le bouton Retour physique du téléphone
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Si le tiroir est ouvert, le fermer
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Sinon, effectuer l'action de retour par défaut
            super.onBackPressed()
        }
    }
}