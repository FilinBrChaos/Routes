package com.example.routes.RoutesListActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.RouteCardBinding

class RoutesListCardAdapter(val routes: ArrayList<RouteDTO>): RecyclerView.Adapter<RoutesListCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutesListCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = RouteCardBinding.inflate(from, parent, false)
        return RoutesListCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoutesListCardViewHolder, position: Int) {
        holder.bindCard(routes[position])
    }

    override fun getItemCount(): Int = routes.size
}