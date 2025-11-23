package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout // NÉCESSAIRE
import com.google.android.material.navigation.NavigationView // NÉCESSAIRE

class ProfilActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // 1. Initialisation des composants du Drawer
        drawerLayout = findViewById(R.id.drawer_layout_profile)
        val navView: NavigationView = findViewById(R.id.nav_view_profile)
        val menuButton: ImageButton = findViewById(R.id.btn_menu_profile)

        // Ouvre le tiroir au clic du bouton menu
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        // 2. Configuration du listener pour les éléments du menu latéral
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> { // ID défini dans drawer_menu.xml
                    performLogout()
                    drawerLayout.closeDrawers()
                    true // Indique que l'événement a été géré
                }
                // Gérez ici d'autres éléments de menu si nécessaire
                else -> false
            }
        }
    }

    /**
     * Centralise la logique de déconnexion : efface la session et redirige vers LoginActivity.
     * Cette fonction est la même que celle utilisée dans HomePage.kt.
     */
    private fun performLogout() {
        // Supprimer les données de session stockées (TOKEN, ROLE, etc.)
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Rediriger vers l'écran de connexion et effacer la pile d'activités
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish() // Fermer ProfilActivity
    }
}