package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page) // Assurez-vous que c'est le bon nom de fichier

        // 1. Trouver l'onglet par l'ID que nous venons d'ajouter
        val pubTab: LinearLayout = findViewById(R.id.nav_pub_tab)

        // 2. Définir l'action au clic (OnClickListener)
        pubTab.setOnClickListener {
            // Créer un Intent pour aller de HomeActivity à MyAnnouncementsActivity
            val intent = Intent(this, MyAnnouncementsActivity::class.java)
            startActivity(intent)
        }

        // ... Reste du code onCreate
    }
}