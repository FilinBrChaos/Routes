package com.example.routes.globalSettings

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.ColorCardBinding

class GlobalSettingsCardAdapter(
    private val callerActivity: Activity,
    private val colors: List<MyColor>,
    private val dbManager: DbManager,
): RecyclerView.Adapter<GlobalSettingsCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlobalSettingsCardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ColorCardBinding.inflate(from, parent, false)
        return GlobalSettingsCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GlobalSettingsCardViewHolder, position: Int) {
        holder.bindColor(callerActivity, colors[position], position, dbManager)
    }

    override fun getItemCount(): Int = colors.size
}