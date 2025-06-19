package com.example.minipriceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class Request(
    val requestText: String = "",
    val imageUrl: String? = null,
    val documentId: String = ""
)

class RequestsAdapter(
    private val requests: MutableList<Request>,
    private val onDeleteClick: (Request, Int) -> Unit
) : RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

    class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewRequestDescription)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewRequest)
        val deleteButton: Button = itemView.findViewById(R.id.buttonDeleteRequest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        holder.descriptionTextView.text = request.requestText

        if (!request.imageUrl.isNullOrEmpty()) {
            holder.imageView.visibility = View.VISIBLE
            Glide.with(holder.imageView.context)
                .load(request.imageUrl)
                .into(holder.imageView)
        } else {
            holder.imageView.visibility = View.GONE
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(request, position)
        }
    }

    fun removeAt(position: Int) {
        requests.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = requests.size
}
