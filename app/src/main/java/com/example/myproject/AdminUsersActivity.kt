package com.example.myproject // IMPORTANT: Remplacez par votre nom de package réel

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminUsersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter // Nous allons créer cet adaptateur plus tard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users) // Nouveau layout à créer

        // Gérer le bouton Retour dans l'en-tête
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recycler_view_users)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 1. Charger les données (Simulées ici)
        val userList = getMockUserList()

        // 2. Initialiser et définir l'adaptateur
        userAdapter = UserAdapter(userList) { userId, action ->
            // Logique de clic sur Approve/Refuse
            handleUserAction(userId, action)
        }
        recyclerView.adapter = userAdapter
    }

    // Fonction de simulation de données
    private fun getMockUserList(): List<User> {
        return listOf(
            User(1, "Alice Dupont", "alice.dupont@mail.com", "User", "pending"),
            User(2, "Bob Martin", "bob.martin@mail.com", "Association", "pending"),
            User(3, "Charlie Leblanc", "charlie@mail.com", "User", "approved"),
            User(4, "Replate NGO", "ngo@replate.org", "Association", "refused")
        )
    }

    /**
     * Gère les clics sur les boutons Approve et Refuse.
     */
    private fun handleUserAction(userId: Int, action: String) {
        // Envoi de la requête au serveur pour mettre à jour le statut
        Toast.makeText(this, "Action '$action' effectuée pour l'utilisateur ID: $userId", Toast.LENGTH_SHORT).show()

        // Mettre à jour la liste après l'action (en environnement réel, vous rechargeriez les données)
        // userAdapter.updateStatus(userId, action)
    }
}

// Classe de données pour l'utilisateur
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    var status: String // pending, approved, refused
)