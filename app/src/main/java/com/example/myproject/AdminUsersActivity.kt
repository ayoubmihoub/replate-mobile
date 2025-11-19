package com.example.myproject

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.ui.adapter.UserAdapter
import com.example.myproject.ui.admin.PendingAccountsViewModel
import android.content.Intent
import androidx.compose.material3.Button
import androidx.wear.compose.material.Button
import com.example.myproject.ui.admin.PendingAccountsViewModelFactory

class AdminUsersActivity : AppCompatActivity() {

    private val TAG = "AdminUsersActivity"

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: PendingAccountsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users)

        val sharedPref = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
        val userToken = sharedPref.getString("TOKEN", "") ?: ""

        if (userToken.isEmpty()) {
            Toast.makeText(this, "Session expirée, reconnectez-vous.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // 🔥 LE BON LISTENER (UN SEUL)


        val token = if (!userToken.startsWith("Bearer ")) "Bearer $userToken" else userToken
        Log.d(TAG, "Token JWT: $token")

        val factory = PendingAccountsViewModelFactory(token)
        viewModel = ViewModelProvider(this, factory).get(PendingAccountsViewModel::class.java)

        recyclerView = findViewById(R.id.recycler_view_users)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(emptyList()) { userId, action -> handleUserAction(userId, action) }
        recyclerView.adapter = userAdapter

        viewModel.pendingUsers.observe(this) {
            userAdapter.updateList(it)
        }
        viewModel.statusMessage.observe(this) {
            if (it.isNotEmpty()) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.fetchPendingAccounts()
    }


    private fun handleUserAction(userId: Long, action: String) {
        when (action.lowercase()) {
            "approve" -> viewModel.validateAccount(userId)
            "refuse" -> viewModel.refuseAccount(userId)
            else -> Toast.makeText(this, "Action inconnue", Toast.LENGTH_SHORT).show()
        }
    }
}
