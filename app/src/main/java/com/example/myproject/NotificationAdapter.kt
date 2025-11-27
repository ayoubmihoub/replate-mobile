package com.example.myproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

// L'adaptateur prend en paramètre une liste d'objets Notification (que nous avons défini dans NotificationActivity)
class NotificationAdapter(private val notifications: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    /**
     * ViewHolder : Détient les références (les vues) pour chaque élément de la liste.
     */
    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconLeft: ImageView = itemView.findViewById(R.id.icon_left)
        val tvTime: TextView = itemView.findViewById(R.id.tv_notification_time)
        val tvMessage: TextView = itemView.findViewById(R.id.tv_notification_message)
        val layoutActions: LinearLayout = itemView.findViewById(R.id.layout_actions)
        val btnDismiss: TextView = itemView.findViewById(R.id.btn_dismiss)
        val btnApprove: TextView = itemView.findViewById(R.id.btn_approve)
        val imageRight: ImageView = itemView.findViewById(R.id.image_right)
    }

    /**
     * Crée de nouvelles vues (appelé par le layout manager).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        // Gonfle la mise en page de l'élément de liste (item_notification.xml)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    /**
     * Remplace le contenu d'une vue par la donnée à une position spécifique.
     */
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]

        // 1. Définir le texte
        holder.tvTime.text = notification.time
        holder.tvMessage.text = notification.message

        // 2. Gérer la visibilité des boutons d'action (Dismiss/Approve)
        if (notification.hasActions) {
            // Afficher les boutons pour les notifications d'offres
            holder.layoutActions.visibility = View.VISIBLE

            // Vous pouvez définir ici des listeners pour les boutons Dismiss et Approve
            holder.btnApprove.setOnClickListener {
                // Logique pour accepter l'offre
            }
            holder.btnDismiss.setOnClickListener {
                // Logique pour refuser l'offre
            }
        } else {
            // Cacher les boutons pour les autres notifications (expiration, paiement complet, etc.)
            holder.layoutActions.visibility = View.GONE
        }

        // 3. Gérer les icônes à gauche et le style
        val context = holder.itemView.context

        // Logique simplifiée pour changer les icônes et les couleurs en fonction du type
        if (notification.message.contains("new offer")) {
            // Icône/Avatar pour une nouvelle offre
            holder.iconLeft.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile)) // Utilisez votre drawable d'avatar
            holder.iconLeft.setBackgroundResource(R.drawable.circle_background_grey) // Optionnel: pour simuler un avatar rond
        } else if (notification.message.contains("expires tomorrow")) {
            // Icône d'avertissement pour l'expiration
            holder.iconLeft.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.free)) // Assurez-vous d'avoir warning_icon.xml
            holder.iconLeft.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        } else if (notification.message.contains("payment has been completed")) {
            // Icône de succès pour le paiement
            holder.iconLeft.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.donee)) // Assurez-vous d'avoir check_circle_icon.xml
            holder.iconLeft.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        } else {
            // Icône par défaut
            holder.iconLeft.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile))
            holder.iconLeft.setBackgroundResource(R.drawable.circle_background_grey)
        }

        // 4. Définir l'image de droite (logique de chargement d'image réelle omise)
        // Normalement, vous utiliseriez une librairie comme Glide ou Coil ici.
        // holder.imageRight.setImageDrawable(...)
    }

    /**
     * Retourne le nombre total d'éléments dans la liste.
     */
    override fun getItemCount(): Int {
        return notifications.size
    }
}