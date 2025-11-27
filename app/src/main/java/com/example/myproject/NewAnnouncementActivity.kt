package com.example.myproject

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context // Ajouté pour SharedPreferences
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.myproject.data.model.Announcement // Importé
import com.example.myproject.data.model.AnnouncementRequest // Importé
import com.example.myproject.data.model.AnnouncementType // Importé
import com.example.myproject.data.remote.RetrofitClient // Importé pour l'appel API
import retrofit2.Call // Importé
import retrofit2.Callback // Importé
import retrofit2.Response // Importé
import java.text.SimpleDateFormat
import java.util.*

class NewAnnouncementActivity : AppCompatActivity() {

    // Constantes pour la sélection d'image
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null // URI pour stocker l'image sélectionnée

    // Références de vues
    private lateinit var expirationDateEditText: EditText
    private lateinit var publishButton: Button
    private lateinit var cancelButton: Button
    private lateinit var cardAddPhoto: CardView
    private lateinit var inputFoodTitle: EditText
    private lateinit var inputQuantity: EditText
    private lateinit var inputContactNumber: EditText
    private lateinit var inputPickupAddress: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_announcement)

        // 1. Initialisation des Vues (utilisant les IDs ajoutés)
        expirationDateEditText = findViewById(R.id.edit_text_expiration_date)
        publishButton = findViewById(R.id.btn_publish)
        cancelButton = findViewById(R.id.btn_cancel)
        cardAddPhoto = findViewById(R.id.card_add_photo)
        inputFoodTitle = findViewById(R.id.input_food_title)
        inputQuantity = findViewById(R.id.input_quantity)
        inputContactNumber = findViewById(R.id.input_contact_number)
        inputPickupAddress = findViewById(R.id.input_pickup_address)

        // Gérer le bouton Retour
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // 2. GESTION DES ÉVÉNEMENTS

        // Événement pour le sélecteur d'image
        cardAddPhoto.setOnClickListener {
            openFileChooser()
        }

        // Événement pour le sélecteur de date
        expirationDateEditText.setOnClickListener {
            showDatePicker()
        }

        // Événement pour le bouton "Publish" (LOGIQUE MODIFIÉE)
        publishButton.setOnClickListener {
            if (validateFormInputs()) {
                val token = getAuthToken() // 1. Récupérer le token
                if (token != null) {
                    publishAnnouncement(token) // 2. Appeler l'API de publication
                } else {
                    Toast.makeText(this, "Erreur d'authentification: Jeton manquant. Veuillez vous reconnecter.", Toast.LENGTH_LONG).show()
                    // Si le jeton est manquant, cela peut arriver si l'utilisateur n'est pas connecté.
                }
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs requis (*).", Toast.LENGTH_LONG).show()
            }
        }

        // Événement pour le bouton "Cancel"
        cancelButton.setOnClickListener {
            finish()
        }
    }

    // -----------------------------------------------------------------------
    // LOGIQUE DE SÉLECTION D'IMAGE et ACTIVITÉ RESULTAT
    // -----------------------------------------------------------------------
    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            Toast.makeText(this, "Image sélectionnée! URI: $imageUri", Toast.LENGTH_SHORT).show()
        }
    }

    // -----------------------------------------------------------------------
    // LOGIQUE DE PUBLICATION D'ANNONCE ET GESTION DU TOKEN (NOUVEAU)
    // -----------------------------------------------------------------------

    /**
     * Placeholder pour la récupération du jeton JWT.
     * REMPLACEZ 'auth_prefs' et 'jwt_token' par vos clés réelles.
     */
    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val tokenFromStorage = sharedPreferences.getString("jwt_token", null)

        // Assurez-vous d'avoir le préfixe "Bearer " pour l'authentification
        return tokenFromStorage?.let { "Bearer $it" }
    }


    private fun publishAnnouncement(authToken: String) {
        // NOTE: Puisque le champ 'description' est requis mais manquant dans l'UI,
        // nous construisons ici une description à partir d'autres champs.
        val descriptionText = "Type: ${inputFoodTitle.text.toString().trim()}, Quantité: ${inputQuantity.text.toString().trim()}. Adresse: ${inputPickupAddress.text.toString().trim()}"

        val requestBody = AnnouncementRequest(
            title = inputFoodTitle.text.toString().trim(),
            description = descriptionText, // Utilisation de la description construite
            price = 0.0, // Assumons 0.0 pour une donation si c'est le cas par défaut
            announcementType = AnnouncementType.DONATION, // Assumons le type par défaut
            imageUrl1 = imageUri?.toString(), // Envoi de l'URI comme URL (à ajuster si un service d'upload est nécessaire)
            expiryDate = expirationDateEditText.text.toString().trim()
        )

        RetrofitClient.api.createAnnouncement(requestBody, authToken).enqueue(object : Callback<Announcement> {
            override fun onResponse(call: Call<Announcement>, response: Response<Announcement>) {
                if (response.isSuccessful) {
                    // Succès de la publication
                    Toast.makeText(this@NewAnnouncementActivity, "Annonce publiée avec succès!", Toast.LENGTH_SHORT).show()
                    showAnnouncementCreatedDialog()
                } else if (response.code() == 401) {
                    // Erreur 401: Non autorisé. Le token est invalide/expiré/manquant.
                    Toast.makeText(this@NewAnnouncementActivity, "Erreur de publication (401): Jeton non valide. Veuillez vous reconnecter.", Toast.LENGTH_LONG).show()
                } else {
                    // Autres erreurs HTTP
                    Toast.makeText(this@NewAnnouncementActivity, "Erreur de publication: Code ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Announcement>, t: Throwable) {
                // Erreur de connexion ou autre échec
                Toast.makeText(this@NewAnnouncementActivity, "Échec de la connexion au serveur: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // -----------------------------------------------------------------------
    // LOGIQUE DE VALIDATION DE FORMULAIRE (Non Modifiée)
    // -----------------------------------------------------------------------
    private fun validateFormInputs(): Boolean {
        var isValid = true

        // 1. Food Title
        if (inputFoodTitle.text.toString().trim().isEmpty()) {
            inputFoodTitle.error = "Le titre est requis."
            isValid = false
        } else {
            inputFoodTitle.error = null
        }

        // 2. Quantity
        if (inputQuantity.text.toString().trim().isEmpty()) {
            inputQuantity.error = "La quantité est requise."
            isValid = false
        } else {
            inputQuantity.error = null
        }

        // 3. Expiration Date
        if (expirationDateEditText.text.toString().trim().isEmpty()) {
            expirationDateEditText.error = "La date est requise."
            isValid = false
        } else {
            expirationDateEditText.error = null
        }

        // 4. Contact Number (Validation simple: non vide)
        val contactNumber = inputContactNumber.text.toString().trim()
        if (contactNumber.isEmpty() || contactNumber.length < 8) {
            inputContactNumber.error = "Le numéro de contact est invalide."
            isValid = false
        } else {
            inputContactNumber.error = null
        }

        // 5. Pickup Address
        if (inputPickupAddress.text.toString().trim().isEmpty()) {
            inputPickupAddress.error = "L'adresse est requise."
            isValid = false
        } else {
            inputPickupAddress.error = null
        }

        return isValid
    }

    // -----------------------------------------------------------------------
    // Fonctions d'interface (Date Picker et Dialogue - Non Modifiées)
    // -----------------------------------------------------------------------
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                expirationDateEditText.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showAnnouncementCreatedDialog() {
        // NOTE: Assurez-vous que le layout R.layout.dialog_announcement_created existe
        val dialogView = layoutInflater.inflate(R.layout.dialog_announcement_created, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val okButton = dialogView.findViewById<Button>(R.id.btn_dialog_ok)

        okButton.setOnClickListener {
            dialog.dismiss()
            // Naviguer vers l'activité des annonces (MyAnnouncementsActivity)
            val intent = Intent(this, MyAnnouncementsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }
}