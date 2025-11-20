package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout // <-- AJOUT NÉCESSAIRE
import com.google.android.material.navigation.NavigationView // <-- AJOUT NÉCESSAIRE

class HomePage : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        // Composants du Layout
        val pubTab: LinearLayout = findViewById(R.id.nav_pub_tab)
        val directLogoutButton = findViewById<ImageButton>(R.id.btn_logout) // Bouton de déconnexion/settings existant
        val menuButton: ImageButton = findViewById(R.id.btn_menu) // Bouton hamburger pour le tiroir

        // --- 1. Initialisation du Tiroir ---
        // L'ID 'drawer_layout' vient de la modification de home_page.xml
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        // --- 2. GESTION DU TIROIR ---
        // Ouvre le tiroir au clic du bouton hamburger
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        // Configuration du listener pour les éléments du menu latéral (Déconnexion)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> { // ID défini dans drawer_menu.xml
                    performLogout()
                    drawerLayout.closeDrawers()
                    true // Indique que l'événement a été géré
                }
                // Gérez ici d'autres éléments du menu si nécessaire
                else -> false
            }
        }

        // --- 3. LOGIQUE DES BOUTONS EXISTANTS ---
        // Le bouton de déconnexion direct est mis à jour pour utiliser la fonction performLogout()
        directLogoutButton.setOnClickListener {
            performLogout()
        }

        pubTab.setOnClickListener {
            val intent = Intent(this, MyAnnouncementsActivity::class.java)
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