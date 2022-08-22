package com.example.routes.appFragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.ColorCardBinding
import com.example.routes.globalSettings.GlobalSettingsCardViewHolder

class LocalSettingsCardAdapter(
    private val colors: ArrayList<MyColor>,
    private val updateRecyclerView: () -> Unit
): RecyclerView.Adapter<LocalSettingsCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalSettingsCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ColorCardBinding.inflate(from, parent, false)
        return LocalSettingsCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocalSettingsCardViewHolder, position: Int) {
        holder.bindCard(position, colors, updateRecyclerView)
    }

    override fun getItemCount(): Int = colors.size
}