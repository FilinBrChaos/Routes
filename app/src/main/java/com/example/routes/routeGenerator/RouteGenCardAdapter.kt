package com.example.routes.routeGenerator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.appFragments.LocalSettingsCardViewHolder
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.CardForRandomRouteBinding
import com.example.routes.databinding.ColorCardBinding

class RouteGenCardAdapter(
    private val colors: List<MyColor>
): RecyclerView.Adapter<RouteGenCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteGenCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardForRandomRouteBinding.inflate(from, parent, false)
        return RouteGenCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteGenCardViewHolder, position: Int) {
        holder.bindCard(colors[position])
    }

    override fun getItemCount(): Int = colors.size
}