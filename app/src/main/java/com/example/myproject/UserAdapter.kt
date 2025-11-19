package com.example.myproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.R
import com.example.myproject.data.model.User

/**
 * Adaptateur pour afficher la liste des utilisateurs en attente (Admin).
 */
class UserAdapter(
    private var users: List<User>,
    private val onUserActionListener: (userId: Long, action: String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.text_user_name)
        val emailTextView: TextView = itemView.findViewById(R.id.text_user_email)
        val roleTextView: TextView = itemView.findViewById(R.id.text_user_role)
        val statusTextView: TextView = itemView.findViewById(R.id.text_validation_status)
        val approveButton: Button = itemView.findViewById(R.id.btn_approve)
        val refuseButton: Button = itemView.findViewById(R.id.btn_refuse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_admin, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        // --- Remplissage de l’UI ---
        holder.nameTextView.text = user.username
        holder.emailTextView.text = user.email
        holder.roleTextView.text = "Role: ${user.role.name}"
        holder.statusTextView.text = if (user.isValidatedByAdmin) "Approved" else "Pending"

        // --- Boutons visibles uniquement si compte non validé ---
        val isPending = !user.isValidatedByAdmin
        holder.approveButton.visibility = if (isPending) View.VISIBLE else View.GONE
        holder.refuseButton.visibility = if (isPending) View.VISIBLE else View.GONE

        if (isPending) {
            holder.approveButton.setOnClickListener {
                onUserActionListener(user.id, "approve")
            }
            holder.refuseButton.setOnClickListener {
                onUserActionListener(user.id, "refuse")
            }
        }
    }

    override fun getItemCount() = users.size

    fun updateList(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
