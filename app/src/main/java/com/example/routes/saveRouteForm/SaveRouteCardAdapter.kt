package com.example.routes.saveRouteForm

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.databinding.ImageCardBinding

class SaveRouteCardAdapter(private val images: ArrayList<Uri>, private val resultLauncher: ActivityResultLauncher<Intent>): RecyclerView.Adapter<SaveRouteCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveRouteCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ImageCardBinding.inflate(from, parent, false)
        return SaveRouteCardViewHolder(binding, resultLauncher)
    }

    override fun onBindViewHolder(holder: SaveRouteCardViewHolder, position: Int) {
        holder.bindCard(images[position])
    }

    override fun getItemCount(): Int = images.size
}