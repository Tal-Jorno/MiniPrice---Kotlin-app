package com.example.minipriceapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RequestProductActivity : AppCompatActivity() {

    private lateinit var editTextRequest: EditText
    private lateinit var imageViewPreview: ImageView
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonSubmit: Button
    private lateinit var buttonBack: Button
    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_product)

        editTextRequest = findViewById(R.id.editTextRequest)
        imageViewPreview = findViewById(R.id.imageViewPreview)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonSubmit = findViewById(R.id.buttonSubmitRequest)
        buttonBack = findViewById(R.id.buttonBack)

        buttonBack.setOnClickListener {
            finish()
        }

        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        buttonSubmit.setOnClickListener {
            val requestText = editTextRequest.text.toString().trim()

            if (requestText.isEmpty()) {
                Toast.makeText(this, "Please enter a request", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
            val username = prefs.getString("loggedInUser", "unknown") ?: "unknown"

            if (selectedImageUri != null) {
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("requests/${UUID.randomUUID()}.jpg")

                imageRef.putFile(selectedImageUri!!)
                    .continueWithTask { task ->
                        if (!task.isSuccessful) {
                            throw task.exception ?: Exception("Image upload failed")
                        }
                        imageRef.downloadUrl
                    }
                    .addOnSuccessListener { uri ->
                        saveRequestToFirestore(username, requestText, uri.toString())
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
            } else {
                saveRequestToFirestore(username, requestText, null)
            }
        }
    }

    private fun saveRequestToFirestore(username: String, requestText: String, imageUrl: String?) {
        val db = FirebaseFirestore.getInstance()

        val request = hashMapOf(
            "username" to username,
            "requestText" to requestText,
            "timestamp" to Timestamp.now()
        )
        if (imageUrl != null) {
            request["imageUrl"] = imageUrl
        }

        db.collection("requests")
            .add(request)
            .addOnSuccessListener {
                Toast.makeText(this, "Request submitted", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.e("REQUEST_FIRESTORE", "Error: ${it.message}", it)
                Toast.makeText(this, "Failed to submit request", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            imageViewPreview.setImageURI(selectedImageUri)
        }
    }
}
