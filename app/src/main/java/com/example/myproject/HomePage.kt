package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val pubTab: LinearLayout = findViewById(R.id.nav_pub_tab)
        val logoutButton = findViewById<ImageButton>(R.id.btn_logout)

        logoutButton.setOnClickListener {
            val sharedPref = getSharedPreferences("my_app_prefs", MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        pubTab.setOnClickListener {
            val intent = Intent(this, MyAnnouncementsActivity::class.java)
            startActivity(intent)
        }
    }
}
