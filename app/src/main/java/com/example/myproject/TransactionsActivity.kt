package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.models.Transaction // Assurez-vous que l'import est correct

// L'interface TransactionActionListener doit être définie dans votre projet
// (soit dans l'adapter, soit dans un fichier séparé)

class TransactionsActivity : AppCompatActivity(), TransactionActionListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnBack: ImageButton

    // Liste réelle des transactions (Mutable)
    private lateinit var transactionsList: MutableList<Transaction>
    private lateinit var transactionsAdapter: TransactionsAdapter

    // Déclarations des onglets de navigation
    private lateinit var navHomeTab: LinearLayout
    private lateinit var navExploreTab: LinearLayout
    private lateinit var navPubTab: LinearLayout
    private lateinit var navOffersTab: LinearLayout
    private lateinit var navProfileTab: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        // Charger les données (filtrées)
        transactionsList = createMockTransactions().toMutableList()

        // 🛑 Fonctions manquantes appelées ici 🛑
        initializeViews()
        setupClickListeners()
        setupBottomNavigation()
        setupRecyclerView()
    }

    // --- 🛑 FONCTION RÉINTÉGRÉE 🛑 ---
    private fun initializeViews() {
        btnBack = findViewById(R.id.btn_back)
        recyclerView = findViewById(R.id.recycler_transactions)

        // Initialisation des onglets de navigation par ID
        navHomeTab = findViewById(R.id.nav_home_tab)
        navExploreTab = findViewById(R.id.nav_explore_tab)
        navPubTab = findViewById(R.id.nav_pub_tab)
        navOffersTab = findViewById(R.id.nav_offers_tab)
        navProfileTab = findViewById(R.id.nav_profile_tab)
    }

    // --- 🛑 FONCTION RÉINTÉGRÉE 🛑 ---
    private fun setupClickListeners() {
        // Bouton Retour
        btnBack.setOnClickListener {
            finish()
        }

        // Bouton Options
        findViewById<ImageButton>(R.id.btn_options).setOnClickListener {
            Toast.makeText(this, "Options cliquées", Toast.LENGTH_SHORT).show()
        }

        // Bouton de tri
        findViewById<TextView>(R.id.btn_sort_newest).setOnClickListener {
            Toast.makeText(this, "Tri activé", Toast.LENGTH_SHORT).show()
        }
    }

    // --- 🛑 FONCTION RÉINTÉGRÉE 🛑 ---
    private fun setupBottomNavigation() {
        // Navigation vers Home
        navHomeTab.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
            finish()
        }

        // Navigation vers Explore (SearchActivity)
        navExploreTab.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
        }

        // Navigation vers Pub (MyAnnouncementsActivity)
        navPubTab.setOnClickListener {
            startActivity(Intent(this, MyAnnouncementsActivity::class.java))
            finish()
        }

        // Navigation vers Profile
        navProfileTab.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
            finish()
        }

        // L'onglet Offers (navOffersTab) est l'activité actuelle, aucun écouteur de navigation n'est nécessaire.
    }

    // ... (Le reste des fonctions est inchangé et correct) ...

    private fun setupRecyclerView() {
        // La liste est maintenant passée en tant que MutableList
        transactionsAdapter = TransactionsAdapter(transactionsList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionsAdapter
    }

    private fun createMockTransactions(): List<Transaction> {
        val allTransactions = listOf(
            Transaction("Homeless support Bizerte", "Local association", "Downtown Bakery", "Pending", "20 - 12 - 2025", true),
            Transaction("Mohamed ben Younes", "Individual", "Downtown Bakery", "Accepted", "20 - 12 - 2025", false),
            Transaction("Association Nord", "Local association", "Pastry Shop 2000", "Pending", "19 - 12 - 2025", true),
            Transaction("Souha Sassi", "Individual", "Couscous Maison", "Rejected", "18 - 12 - 2025", false)
        )

        // FILTRER POUR N'AFFICHER QUE LES OFFRES PENDING
        return allTransactions.filter { it.status == "Pending" }
    }

    // --- GESTION DES ACTIONS D'OFFRE ---

    override fun onOfferAccepted(position: Int, transaction: Transaction) {
        // 1. Mise à jour de la base de données (non implémentée ici)

        // 2. Supprimer l'élément de la liste
        transactionsList.removeAt(position)

        // 3. Notifier l'Adapter du changement
        transactionsAdapter.notifyItemRemoved(position)

        Toast.makeText(this, "Offre de ${transaction.senderName} acceptée et retirée de la liste.", Toast.LENGTH_LONG).show()
    }

    override fun onOfferRejected(position: Int, transaction: Transaction) {
        // 1. Mise à jour de la base de données (non implémentée ici)

        // 2. Supprimer l'élément de la liste
        transactionsList.removeAt(position)

        // 3. Notifier l'Adapter du changement
        transactionsAdapter.notifyItemRemoved(position)

        Toast.makeText(this, "Offre de ${transaction.senderName} rejetée et retirée de la liste.", Toast.LENGTH_LONG).show()
    }
}