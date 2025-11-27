package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Home2Activity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_2)

        drawerLayout = findViewById(R.id.drawer_layout)

        val menuButton: ImageButton = findViewById(R.id.btn_menu)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(findViewById<NavigationView>(R.id.nav_view))
        }

        setupDrawerNavigation()
        setupBottomNavigation()
        setupDonationCardsNavigation()
    }

    private fun setupDrawerNavigation() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    performLogout()
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupBottomNavigation() {
        val profileTab: LinearLayout = findViewById(R.id.nav_profile_tab)
        profileTab.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }
    }

    private fun setupDonationCardsNavigation() {
        findViewById<FrameLayout>(R.id.donation_card_free).setOnClickListener {
            navigateToAnnouncementInfo("FREE001")
        }
        findViewById<FrameLayout>(R.id.donation_card_sale).setOnClickListener {
            navigateToAnnouncementInfo("SALE001")
        }
        findViewById<FrameLayout>(R.id.donation_card_nearby).setOnClickListener {
            navigateToAnnouncementInfo("NEARBY001")
        }
        findViewById<FrameLayout>(R.id.donation_card_nearby_2).setOnClickListener {
            navigateToAnnouncementInfo("NEARBY002")
        }
    }

    private fun navigateToAnnouncementInfo(announcementId: String) {
        val intent = Intent(this, AnnouncementInfoActivity::class.java)
        intent.putExtra("ANNOUNCEMENT_ID", announcementId)
        startActivity(intent)
    }

    private fun performLogout() {
        val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
