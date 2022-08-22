package com.example.routes.appFragments

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.AppRuntimeData
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.ColorCardBinding

class LocalSettingsCardViewHolder(private val colorCardBinding: ColorCardBinding): RecyclerView.ViewHolder(colorCardBinding.root) {
    fun bindCard(position: Int, colors: ArrayList<MyColor>, updateRecyclerView: () -> Unit){
        colorCardBinding.colorPreview.setBackgroundColor(Color.parseColor(colors[position].colorValue))
        colorCardBinding.colorNameText.text = colors[position].colorName
        colorCardBinding.deleteButton.setOnClickListener {
            AppRuntimeData.colorsListInLocalSettings.removeAt(position)
            updateRecyclerView()
        }
    }
}