package com.example.myproject // IMPORTANT: Remplacez par votre nom de package réel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Définition du type de fonction pour le callback de clic
typealias OnUserActionListener = (userId: Int, action: String) -> Unit

class UserAdapter(
    private val userList: List<User>,
    private val listener: OnUserActionListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameText: TextView = itemView.findViewById(R.id.text_username)
        val emailText: TextView = itemView.findViewById(R.id.text_email)
        val statusText: TextView = itemView.findViewById(R.id.text_status)
        val layoutActions: View = itemView.findViewById(R.id.layout_actions)
        val approveButton: Button = itemView.findViewById(R.id.btn_approve)
        val refuseButton: Button = itemView.findViewById(R.id.btn_refuse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_approval, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.usernameText.text = "${user.username} (${user.role})"
        holder.emailText.text = user.email

        // Gérer l'affichage des boutons et du statut en fonction du statut
        when (user.status) {
            "pending" -> {
                holder.layoutActions.visibility = View.VISIBLE
                holder.statusText.visibility = View.GONE
            }
            "approved" -> {
                holder.layoutActions.visibility = View.GONE
                holder.statusText.visibility = View.VISIBLE
                holder.statusText.text = "Status: Approved"
                holder.statusText.setTextColor(holder.itemView.context.resources.getColor(R.color.green_accent)) // Assurez-vous d'avoir cette couleur
            }
            "refused" -> {
                holder.layoutActions.visibility = View.GONE
                holder.statusText.visibility = View.VISIBLE
                holder.statusText.text = "Status: Refused"
                holder.statusText.setTextColor(holder.itemView.context.resources.getColor(R.color.red_accent)) // Assurez-vous d'avoir cette couleur
            }
        }

        // Configuration des listeners de boutons
        holder.approveButton.setOnClickListener {
            listener.invoke(user.id, "approved")
        }

        holder.refuseButton.setOnClickListener {
            listener.invoke(user.id, "refused")
        }
    }

    override fun getItemCount() = userList.size
}