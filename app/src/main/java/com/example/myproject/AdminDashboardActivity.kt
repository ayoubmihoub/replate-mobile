package com.example.myproject // IMPORTANT: Remplacez par votre nom de package réel

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AdminDashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Assurez-vous d'avoir le layout admin_dashboard_mobile.xml
        setContentView(R.layout.admin_dashboard_mobile)

        // 1. Initialisation des vues
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        // 2. Configuration de la Toolbar comme ActionBar
        setSupportActionBar(toolbar)
        // Masque le titre par défaut pour laisser la place au TextView personnalisé ou au logo
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 3. Configuration du DrawerLayout et du bouton "hamburger"
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open, // À définir dans strings.xml
            R.string.navigation_drawer_close // À définir dans strings.xml
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 4. Définir le listener pour les éléments du menu de navigation
        navigationView.setNavigationItemSelectedListener(this)

        // 5. Configuration de la gestion moderne du bouton/geste de retour
        setupOnBackPressedDispatcher()

        // Sélection par défaut
        navigationView.setCheckedItem(R.id.nav_dashboard)
    }

    /**
     * Configure le OnBackPressedDispatcher pour gérer le DrawerLayout.
     * Le tiroir est prioritaire sur l'action de retour par défaut.
     */
    private fun setupOnBackPressedDispatcher() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Si le tiroir est ouvert, le fermer.
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Sinon, utiliser le comportement par défaut (fermer l'activité).
                    isEnabled = false // Désactiver temporairement ce callback
                    onBackPressedDispatcher.onBackPressed() // Appeler le système
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    /**
     * Gère les clics sur les éléments du menu de navigation latérale.
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_dashboard -> {
                Toast.makeText(this, "Dashboard sélectionné", Toast.LENGTH_SHORT).show()
                // Logique de chargement du fragment/vue Dashboard
            }
            R.id.nav_analytics -> {
                Toast.makeText(this, "Analytics sélectionné", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_users -> {
                // Lancer l'activité de gestion des utilisateurs
                val intent = Intent(this, AdminUsersActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_associations -> {
                Toast.makeText(this, "Associations sélectionné", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_donations -> {
                Toast.makeText(this, "Donations sélectionné", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_reports -> {
                Toast.makeText(this, "Reports sélectionné", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_tickets -> {
                Toast.makeText(this, "Tickets sélectionné", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Settings sélectionné", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Déconnexion...", Toast.LENGTH_SHORT).show()
                // Logique de déconnexion et redirection vers l'écran de connexion
            }
        }

        // Ferme le tiroir de navigation après la sélection
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}