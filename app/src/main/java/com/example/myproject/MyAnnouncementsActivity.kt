package com.example.myproject





import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MyAnnouncementsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_announcments)


        // 1. Trouver le bouton par l'ID
        val newAnnouncementButton = findViewById<CardView>(R.id.btn_new_announcement)

        // 2. Définir l'action au clic
        newAnnouncementButton.setOnClickListener {
            // Créer un Intent pour aller à NewAnnouncementActivity
            val intent = Intent(this, NewAnnouncementActivity::class.java)
            startActivity(intent)
        }

        // ... (Reste de votre code onCreate)
    }

}