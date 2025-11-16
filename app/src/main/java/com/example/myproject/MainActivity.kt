package com.example.myproject



import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        // 1. Trouver le bouton par son ID (button_next)
        val nextButton: Button = findViewById(R.id.button_next)

        // 2. Définir l'écouteur de clic (Click Listener)
        nextButton.setOnClickListener {
            // Cette partie s'exécute lorsque le bouton est cliqué.

            // 3. Créer un Intent pour démarrer la nouvelle activité
            // Assurez-vous d'avoir créé la classe Activity2.kt
            val intent = Intent(this, Activity2::class.java)

            // Démarrer l'activité
            startActivity(intent)
        }
    }
}