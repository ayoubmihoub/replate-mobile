package com.example.myproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AnnouncementInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.announcement_info)

        val announcementId = intent.getStringExtra("ANNOUNCEMENT_ID")

        val backButton = findViewById<ImageButton>(R.id.btn_back_info)
        backButton?.setOnClickListener { finish() }

        val notificationButton = findViewById<ImageButton>(R.id.btn_notification_info)
        notificationButton?.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        val contactBtn = findViewById<Button>(R.id.btn_contact_donor)
        contactBtn?.setOnClickListener {
            Toast.makeText(this, "Contacter le donateur", Toast.LENGTH_SHORT).show()
        }

        val makeOfferBtn = findViewById<Button>(R.id.btn_make_offer)
        makeOfferBtn?.setOnClickListener {
            Toast.makeText(this, "Faire une offre", Toast.LENGTH_SHORT).show()
        }

        val offerJohnView = findViewById<View>(R.id.offer_john_doe)
        offerJohnView?.let {
            it.findViewById<Button>(R.id.btn_approve_offer)?.setOnClickListener {
                Toast.makeText(this, "Offre John acceptée", Toast.LENGTH_SHORT).show()
            }
            it.findViewById<Button>(R.id.btn_dismiss_offer)?.setOnClickListener {
                Toast.makeText(this, "Offre John refusée", Toast.LENGTH_SHORT).show()
            }
        }

        val offerShelterView = findViewById<View>(R.id.offer_homeless_shelter)
        offerShelterView?.let {
            it.findViewById<Button>(R.id.btn_approve_offer)?.setOnClickListener {
                Toast.makeText(this, "Offre abri acceptée", Toast.LENGTH_SHORT).show()
            }
            it.findViewById<Button>(R.id.btn_dismiss_offer)?.setOnClickListener {
                Toast.makeText(this, "Offre abri refusée", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
