// TransactionsAdapter.kt (Mise à jour)
package com.example.myproject

import android.view.LayoutInflater
import com.example.myproject.models.Transaction
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat // Import nécessaire

// 🛑 NOUVELLE INTERFACE DE CALLBACK 🛑
interface TransactionActionListener {
    fun onOfferAccepted(position: Int, transaction: Transaction)
    fun onOfferRejected(position: Int, transaction: Transaction)
}

class TransactionsAdapter(
    private val transactions: MutableList<Transaction>, // Rendre la liste mutable pour les mises à jour
    private val listener: TransactionActionListener // Ajouter le listener
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    // Définitions des couleurs pour les statuts
    // Note: Déplacez ces couleurs dans un fichier de ressources colors.xml si ce n'est pas déjà fait.

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ... (Déclarations des vues inchangées) ...
        val tvSenderName: TextView = itemView.findViewById(R.id.tv_sender_name)
        val tvEntityType: TextView = itemView.findViewById(R.id.tv_entity_type)
        val tvAnnouncementTitle: TextView = itemView.findViewById(R.id.tv_announcement_title)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val layoutActionButtons: LinearLayout = itemView.findViewById(R.id.layout_action_buttons)
        val btnReject: Button = itemView.findViewById(R.id.btn_reject_offer)
        val btnAccept: Button = itemView.findViewById(R.id.btn_accept_offer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.itemView.context

        holder.tvSenderName.text = transaction.senderName
        holder.tvEntityType.text = transaction.entityType
        holder.tvAnnouncementTitle.text = transaction.announcementTitle
        holder.tvDate.text = transaction.date

        // Gérer le statut et la couleur (Utilisation de ContextCompat recommandée)
        holder.tvStatus.text = transaction.status
        when (transaction.status) {
            "Pending" -> holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.orange_pending))
            "Accepted" -> holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green_accepted))
            else -> holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.grey_rejected))
        }

        // Afficher les boutons d'action seulement si le statut est "Pending" (canAct est true)
        if (transaction.canAct) {
            holder.layoutActionButtons.visibility = View.VISIBLE
        } else {
            holder.layoutActionButtons.visibility = View.GONE
        }

        // 🛑 NOUVEAUX ÉCOUTEURS QUI DÉCLENCHENT LE CALLBACK 🛑
        holder.btnReject.setOnClickListener {
            listener.onOfferRejected(position, transaction)
        }
        holder.btnAccept.setOnClickListener {
            listener.onOfferAccepted(position, transaction)
        }
    }

    override fun getItemCount() = transactions.size
}