package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
// Import pour le TextWatcher si vous implémentez la recherche en temps réel
// import android.text.TextWatcher
// import android.text.Editable

class SearchActivity : AppCompatActivity() {

    // Déclaration des vues
    private lateinit var btnBack: ImageButton
    private lateinit var inputSearch: EditText
    private lateinit var btnFilter: ImageButton
    private lateinit var tvResultsCount: TextView
    private lateinit var layoutNoResults: LinearLayout
    private lateinit var recyclerView: RecyclerView

    // Déclaration des onglets de navigation
    private lateinit var navHomeTab: LinearLayout
    private lateinit var navExploreTab: LinearLayout
    private lateinit var navPubTab: LinearLayout
    private lateinit var navOffersTab: LinearLayout
    private lateinit var navProfileTab: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initializeViews()
        setupClickListeners()
        setupBottomNavigation()

        // Appel de la fonction manquante
        showNoResults(show = true, count = 0)
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btn_back_search)
        inputSearch = findViewById(R.id.input_search_donations)
        btnFilter = findViewById(R.id.btn_filter)
        tvResultsCount = findViewById(R.id.tv_results_count)
        layoutNoResults = findViewById(R.id.layout_no_results)
        recyclerView = findViewById(R.id.recycler_search_results)

        // Initialisation des onglets de navigation
        navHomeTab = findViewById(R.id.nav_home_tab)
        navExploreTab = findViewById(R.id.nav_explore_tab)
        navPubTab = findViewById(R.id.nav_pub_tab)
        navOffersTab = findViewById(R.id.nav_offers_tab)
        navProfileTab = findViewById(R.id.nav_profile_tab)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        btnFilter.setOnClickListener {
            // Logique de filtre ici
        }

        // Si vous implémentez la recherche en temps réel, réactivez ceci :
        /*
        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { performSearch(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        */
    }

    private fun setupBottomNavigation() {
        // Navigation vers Home
        navHomeTab.setOnClickListener {
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }

        // Navigation vers Pub (MyAnnouncementsActivity)
        navPubTab.setOnClickListener {
            startActivity(Intent(this, MyAnnouncementsActivity::class.java))
            finish()
        }

        // Navigation vers Transactions/Offers
        navOffersTab.setOnClickListener {
            startActivity(Intent(this, TransactionsActivity::class.java))
            finish()
        }

        // Navigation vers Profil
        navProfileTab.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
            finish()
        }

        // L'onglet Explore (navExploreTab) est l'activité actuelle, donc il n'y a pas d'écouteur défini.
    }

    // 🛑 FONCTION RÉINTÉGRÉE (Celle qui manquait) 🛑
    /**
     * Gère la visibilité des résultats de recherche.
     */
    private fun showNoResults(show: Boolean, count: Int) {
        if (show) {
            layoutNoResults.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            tvResultsCount.text = "0 founds"
        } else {
            layoutNoResults.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            tvResultsCount.text = "$count founds"
        }
    }

    // 🛑 FONCTION RÉINTÉGRÉE 🛑
    /**
     * Simule la logique de recherche.
     */
    private fun performSearch(query: String) {
        val mockResults = if (query.contains("test", ignoreCase = true)) 15 else 0

        if (mockResults > 0) {
            showNoResults(show = false, count = mockResults)
        } else {
            showNoResults(show = true, count = 0)
        }
    }
}