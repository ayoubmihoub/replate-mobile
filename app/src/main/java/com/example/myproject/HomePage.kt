package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
// REMPLACEMENT : DrawerLayout et NavigationView ne sont plus nécessaires
// import androidx.drawerlayout.widget.DrawerLayout
// import com.google.android.material.navigation.NavigationView

class HomePage : AppCompatActivity() {

    // SUPPRESSION : drawerLayout n'est plus utilisé
    // private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        // Composants du Layout
        val pubTab: LinearLayout = findViewById(R.id.nav_pub_tab)
        val directLogoutButton = findViewById<ImageButton>(R.id.btn_logout)
        // SUPPRESSION : Le bouton menu (hamburger) n'est plus utilisé
        // val menuButton: ImageButton = findViewById(R.id.btn_menu)

        // NOUVEAU : Référence au nouvel ID du bouton Profil dans la barre inférieure
        val profileTab: LinearLayout = findViewById(R.id.nav_profile_tab)

        // --- 1. SUPPRESSION ET SIMPLIFICATION ---
        // SUPPRESSION de la gestion du Tiroir
        // drawerLayout = findViewById(R.id.drawer_layout)
        // val navView: NavigationView = findViewById(R.id.nav_view)

        // SUPPRESSION : L'écouteur du bouton hamburger n'est plus utilisé
        // menuButton.setOnClickListener {
        //     drawerLayout.openDrawer(navView)
        // }

        // SUPPRESSION : L'écouteur du menu latéral n'est plus utilisé
        // navView.setNavigationItemSelectedListener { menuItem ->
        //    when (menuItem.itemId) {
        //        R.id.nav_logout -> {
        //            performLogout()
        //            drawerLayout.closeDrawers()
        //            true
        //        }
        //        else -> false
        //    }
        // }

        // --- 2. LOGIQUE DES BOUTONS EXISTANTS ---
        directLogoutButton.setOnClickListener {
            performLogout()
        }

        pubTab.setOnClickListener {
            val intent = Intent(this, MyAnnouncementsActivity::class.java)
            startActivity(intent)
        }

        // --- 3. NOUVELLE LOGIQUE : BOUTON PROFIL (Barre inférieure) ---
        profileTab.setOnClickListener {
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Centralise la logique de déconnexion : efface la session et redirige vers LoginActivity.
     */
    private fun performLogout() {
        // Supprimer les données de session stockées (TOKEN, ROLE, etc.)
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Rediriger vers l'écran de connexion
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish() // Fermer HomePage
    }
}