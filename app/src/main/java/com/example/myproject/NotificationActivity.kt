package com.example.myproject

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ATTENTION : Le nom du fichier XML doit correspondre à celui que nous avons créé : R.layout.notification
        setContentView(R.layout.notification)

        // --- 1. Gestion du Bouton Retour ---
        val backButton = findViewById<ImageButton>(R.id.btn_back_notifications)
        backButton.setOnClickListener {
            // Ferme cette activité et retourne à l'activité précédente (MyAnnouncementsActivity)
            finish()
        }

        // --- 2. Configuration du RecyclerView pour la liste de notifications ---
        val recyclerView = findViewById<RecyclerView>(R.id.notifications_recycler_view)

        // Nous utilisons LinearLayoutManager pour afficher les éléments en liste verticale
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Dans un cas réel, vous auriez ici : recyclerView.adapter = NotificationAdapter(dataList)
        // Pour cet exemple, nous allons utiliser un adaptateur simulé (NotificationAdapter)
        // et des données pour remplir la liste.

        // Simulation des données de notifications pour l'affichage (basé sur votre image)
        val dummyData = listOf(
            Notification("You've received a new offer from Ahmed Amine for announcement AN-001.", "8 hours ago", true),
            Notification("Your announcement Fresh Bread Basket expires tomorrow.", "8 hours ago", false),
            Notification("You've received a new offer from Imen Azaiz for announcement AN-003.", "8 hours ago", true),
            Notification("The payment for the announcement AN-004 has been completed.", "8 hours ago", false)
            // ... Ajoutez plus d'éléments si nécessaire
        )

        // L'adaptateur doit être défini pour afficher les données
        // Vous devrez implémenter NotificationAdapter (voir la note ci-dessous)
        recyclerView.adapter = NotificationAdapter(dummyData)


        // --- 3. Gestion du TabLayout (Onglets) ---
        val tabLayout = findViewById<TabLayout>(R.id.notification_tab_layout)

        // Ajoutez des écouteurs si vous voulez changer le contenu du RecyclerView en fonction de l'onglet
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // Onglet "Notifications" sélectionné : mettre à jour le RecyclerView avec les données de notifications
                        // Par exemple: recyclerView.adapter = NotificationAdapter(notificationData)
                    }
                    1 -> {
                        // Onglet "Requests" sélectionné : mettre à jour le RecyclerView avec les données de requêtes
                        // Par exemple: recyclerView.adapter = RequestAdapter(requestData)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Non utilisé dans cet exemple
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Non utilisé dans cet exemple
            }
        })
    }
}

// --- Modèle de données simplifié pour une notification ---
data class Notification(
    val message: String,
    val time: String,
    val hasActions: Boolean // Vrai pour les notifications "Offer", Faux pour les autres
)

// --- REMARQUE : Vous devez créer un NotificationAdapter.kt ---
// C'est la classe responsable de lier les données (Notification) à la mise en page (item_notification.xml).
// Sans elle, l'application crashera lors de l'exécution du RecyclerView.