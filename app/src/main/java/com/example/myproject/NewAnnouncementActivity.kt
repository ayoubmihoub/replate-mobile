package com.example.myproject

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.myproject.data.model.AnnouncementRequest
import com.example.myproject.data.model.AnnouncementType
import com.example.myproject.data.remote.ApiService
import com.example.myproject.data.remote.NetworkResult
import com.example.myproject.data.remote.RetrofitClient
import com.example.myproject.data.repository.AnnouncementRepository
import com.example.myproject.data.session.SessionManager
import com.example.myproject.ui.AnnouncementViewModel
import com.example.myproject.ui.AnnouncementViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class NewAnnouncementActivity : AppCompatActivity() {

    private lateinit var viewModel: AnnouncementViewModel

    // Constantes et Vues...
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

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

        // 1. Initialisation des Vues
        expirationDateEditText = findViewById(R.id.edit_text_expiration_date)
        publishButton = findViewById(R.id.btn_publish)
        cancelButton = findViewById(R.id.btn_cancel)
        cardAddPhoto = findViewById(R.id.card_add_photo)
        inputFoodTitle = findViewById(R.id.input_food_title)
        inputQuantity = findViewById(R.id.input_quantity)
        inputContactNumber = findViewById(R.id.input_contact_number)
        inputPickupAddress = findViewById(R.id.input_pickup_address)

        // --- INITIALISATION MVVM (CORRIGÉE) ---
        // Utilisation de RetrofitClient.api (instance de ApiService)
        val apiService = RetrofitClient.api
        val repository = AnnouncementRepository(apiService, SessionManager)
        val factory = AnnouncementViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AnnouncementViewModel::class.java]
        // --------------------------------------

        // Gérer le bouton Retour
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }

        // Événement pour le bouton "Publish"
        publishButton.setOnClickListener {
            if (validateFormInputs()) {
                createAndPublishAnnouncement() // Appel au ViewModel
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs requis (*).", Toast.LENGTH_LONG).show()
            }
        }

        // ... (autres écouteurs)
        cardAddPhoto.setOnClickListener { openFileChooser() }
        expirationDateEditText.setOnClickListener { showDatePicker() }
        cancelButton.setOnClickListener { finish() }

        // 3. OBSERVATION MVVM
        observeCreationStatus()
    }

    // -----------------------------------------------------------------------
    // LOGIQUE DE PRÉSENTATION (Interagit avec le ViewModel)
    // -----------------------------------------------------------------------

    private fun createAndPublishAnnouncement() {
        val title = inputFoodTitle.text.toString().trim()
        val expiryDate = expirationDateEditText.text.toString().trim()
        val quantity = inputQuantity.text.toString().trim()
        val contactNumber = inputContactNumber.text.toString().trim()
        val pickupAddress = inputPickupAddress.text.toString().trim()
        val imageUrl = imageUri?.toString()

        val combinedDescription = "Quantité: $quantity, Contact: $contactNumber, Adresse: $pickupAddress"

        val request = AnnouncementRequest(
            title = title,
            description = combinedDescription,
            price = 0.0,
            announcementType = AnnouncementType.DONATION,
            imageUrl1 = imageUrl,
            expiryDate = expiryDate
        )

        viewModel.createAnnouncement(request)
    }

    private fun observeCreationStatus() {
        // Observation du statut de chargement
        viewModel.isLoading.observe(this) { isLoading ->
            publishButton.isEnabled = !isLoading
        }

        // Observation du résultat de l'opération
        viewModel.creationStatus.observe(this) { result ->
            result?.let {
                when (it) {
                    is NetworkResult.Success -> {
                        showAnnouncementCreatedDialog()
                        viewModel.resetCreationStatus()
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        viewModel.resetCreationStatus()
                    }
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // LOGIQUE UI (Inchagée)
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
        val dialogView = layoutInflater.inflate(R.layout.dialog_announcement_created, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val okButton = dialogView.findViewById<Button>(R.id.btn_dialog_ok)

        okButton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, MyAnnouncementsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
        dialog.show()
    }
}