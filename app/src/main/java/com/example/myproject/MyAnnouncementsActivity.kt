package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MyAnnouncementsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ATTENTION : Changez "my_announcements_activity" par le nom exact de votre fichier XML
        // Par exemple : R.layout.my_announcements
        setContentView(R.layout.my_announcments)


        // --- LOGIQUE DU BOUTON RETOUR ---

        // 1. Trouver le bouton 'Back'
        val backButton = findViewById<ImageButton>(R.id.btn_back)

        // 2. Définir l'action : fermer l'activité pour revenir à l'activité précédente (HomePage)
        backButton.setOnClickListener {
            finish() // Cette fonction est la clé pour le retour en arrière
        }

        // --- LOGIQUE DES BOUTONS D'ACTION (Nouvelle Annonce) ---

        // Trouver la carte d'action "New Announcement"
        val newAnnouncementCard = findViewById<CardView>(R.id.btn_new_announcement)

        // Trouver le bouton de l'état vide "Make an announcement now"
        // (Il est possible que cet ID doive être findViewById<Button> au lieu de CardView)
        val newAnnouncementButtonEmpty = findViewById<Button>(R.id.btn_make_announcement_empty)

        // Définir l'action pour la carte
        newAnnouncementCard.setOnClickListener {
            navigateToNewAnnouncement()
        }

        // Définir l'action pour le bouton de l'état vide
        newAnnouncementButtonEmpty.setOnClickListener {
            navigateToNewAnnouncement()
        }

        // NOTE: Si le bouton de notification (btn_notification) devait naviguer,
        // vous devriez ajouter sa référence et son écouteur ici.
    }

    /**
     * Gère la navigation vers l'écran de création d'annonce.
     * (Assurez-vous d'avoir une classe NewAnnouncementActivity)
     */
    private fun navigateToNewAnnouncement() {
        // Remplacez NewAnnouncementActivity::class.java par le nom de votre classe d'activité de création.
        val intent = Intent(this, NewAnnouncementActivity::class.java)
        startActivity(intent)
    }

}