package com.example.routes.cardsStuff

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.EditDeleteColorCardBinding

class EditableDeletableCardViewHolder(val binding: EditDeleteColorCardBinding) {
    fun bindCard(color: MyColor, deleteButtonHandler: (color: MyColor) -> Unit, editButtonHandler: (color: MyColor) -> Unit){
        binding.colorPreview.setBackgroundColor(Color.parseColor(color.colorValue))
        binding.colorNameText.text = color.colorName

        binding.editButton.setOnClickListener {
            editButtonHandler(color)
        }

        binding.deleteButton.setOnClickListener {
            deleteButtonHandler(color)
        }
    }
}