package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.drawerlayout.widget.DrawerLayout // Import pour le tiroir de navigation
import com.google.android.material.navigation.NavigationView // Import pour la vue de navigation

class Home2Activity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_2)

        // 1. Initialiser le DrawerLayout (doit correspondre à l'ID de la racine XML modifiée)
        drawerLayout = findViewById(R.id.drawer_layout)

        // 2. Trouver le bouton menu (hamburger)
        val menuButton: ImageButton = findViewById(R.id.btn_menu)

        // 3. Ouvrir le tiroir de navigation au clic du bouton menu
        menuButton.setOnClickListener {
            // Ouvre le tiroir à partir du bord 'start' (gauche)
            drawerLayout.openDrawer(findViewById<NavigationView>(R.id.nav_view))
        }

        // 4. Configurer le listener pour les éléments du menu latéral
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    performLogout()
                    // Fermer le tiroir après l'action
                    drawerLayout.closeDrawers()
                    true
                }
                // Ajoutez ici la gestion des autres éléments de menu
                else -> false
            }
        }
    }

    /**
     * Effectue la déconnexion en effaçant les SharedPreferences et en redirigeant.
     */
    private fun performLogout() {
        // Supprimer les données de session stockées
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().apply {
            clear() // Efface le TOKEN, le ROLE, etc.
            apply()
        }

        // Rediriger vers l'écran de connexion
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish() // Fermer Home2Activity
    }
}