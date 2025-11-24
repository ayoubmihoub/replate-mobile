package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        // --- Références des Vues ---

        val directLogoutButton = findViewById<ImageButton>(R.id.btn_logout)
        val pubTab: LinearLayout = findViewById(R.id.nav_pub_tab)
        val exploreTab: LinearLayout = findViewById(R.id.nav_explore_tab)
        val profileTab: LinearLayout = findViewById(R.id.nav_profile_tab)

        // CIBLE DU PROBLÈME : Référence à l'onglet Offers
        val offersTab: LinearLayout = findViewById(R.id.nav_offers_tab)


        // --- LOGIQUE DES BOUTONS ---

        // 1. Déconnexion (Bouton Settings)
        directLogoutButton.setOnClickListener {
            performLogout()
        }

        // 2. Navigation vers MyAnnouncementsActivity (Bouton pub)
        pubTab.setOnClickListener {
            startActivity(Intent(this, MyAnnouncementsActivity::class.java))
        }

        // 3. Navigation vers ProfilActivity (Bouton Profile)
        profileTab.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }

        // 4. Navigation vers SearchActivity (Bouton Explore)
        exploreTab.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // 5. CORRECTION/NAVIGATION VERS TRANSACTIONS (Bouton Offers)
        offersTab.setOnClickListener {
            // L'erreur de fermeture peut venir du fait que TransactionsActivity n'est pas trouvé.
            // Vérifiez que la classe est bien importée et existe.
            val intent = Intent(this, TransactionsActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Centralise la logique de déconnexion : efface la session et redirige vers LoginActivity.
     */
    private fun performLogout() {
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}