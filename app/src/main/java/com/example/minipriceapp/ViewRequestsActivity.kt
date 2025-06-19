package com.example.minipriceapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ViewRequestsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RequestsAdapter
    private val requestList = mutableListOf<Request>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_requests)

        // כפתור חזור
        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewRequests)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = RequestsAdapter(requestList) { request, position ->
            FirebaseFirestore.getInstance()
                .collection("requests")
                .document(request.documentId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Request deleted", Toast.LENGTH_SHORT).show()
                    requestList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to delete request", Toast.LENGTH_SHORT).show()
                }
        }

        recyclerView.adapter = adapter

        loadRequestsFromFirebase()
    }

    private fun loadRequestsFromFirebase() {
        FirebaseFirestore.getInstance()
            .collection("requests")
            .get()
            .addOnSuccessListener { result ->
                requestList.clear()
                for (document in result) {
                    val requestText = document.getString("requestText") ?: ""
                    val imageUrl = document.getString("imageUrl")
                    val documentId = document.id
                    requestList.add(Request(requestText, imageUrl, documentId))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show()
            }
    }
}
