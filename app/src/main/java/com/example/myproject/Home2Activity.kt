package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout // Import nécessaire pour le LinearLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Home2Activity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Assurez-vous que R.layout.home_2 correspond au nom de votre layout XML principal
        setContentView(R.layout.home_2)

        // 1. Initialiser le DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // 2. Trouver le bouton menu (hamburger)
        val menuButton: ImageButton = findViewById(R.id.btn_menu)

        // 3. Ouvrir le tiroir de navigation au clic du bouton menu
        menuButton.setOnClickListener {
            // Ouvre le tiroir à partir du bord 'start' (gauche)
            drawerLayout.openDrawer(findViewById<NavigationView>(R.id.nav_view))
        }

        // 4. Configurer le listener pour les éléments du menu latéral
        setupDrawerNavigation()

        // 5. Configurer la navigation pour la barre inférieure (NOUVEAU)
        setupBottomNavigation()
    }

    private fun setupDrawerNavigation() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    performLogout()
                    drawerLayout.closeDrawers()
                    true
                }
                // Gérez d'autres éléments de menu ici
                else -> false
            }
        }
    }

    private fun setupBottomNavigation() {
        // Trouver l'onglet Profile par son ID
        val profileTab: LinearLayout = findViewById(R.id.nav_profile_tab)

        // Définir l'écouteur de clic pour naviguer vers ProfilActivity
        profileTab.setOnClickListener {
            // Création de l'Intent pour démarrer ProfilActivity
            val intent = Intent(this, ProfilActivity::class.java)
            startActivity(intent)
            // Optionnel : vous pourriez vouloir appeler finish() ici si vous ne voulez pas revenir à Home2Activity.
        }

        // 💡 CONSEIL : Ajoutez la même logique pour les autres onglets ici (Explore, Offers, Home, pub)
        // Par exemple, l'onglet 'pub' a déjà l'ID 'nav_pub_tab' dans votre XML.
        val pubTab: LinearLayout = findViewById(R.id.nav_pub_tab)
        pubTab.setOnClickListener {
            // Exemple : Ne rien faire ou naviguer vers une autre activité
        }
    }

    /**
     * Effectue la déconnexion en effaçant les SharedPreferences et en redirigeant.
     */
    private fun performLogout() {
        // ... (le code performLogout reste inchangé)
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().apply {
            clear()
            apply()
        }

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}