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
    fun bindCard(callerActivity: Activity, color: MyColor, dbManager: DbManager){
        binding.colorPreview.setBackgroundColor(Color.parseColor(color.colorValue))
        binding.colorNameText.text = color.colorName
        binding.deleteButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(callerActivity)
            dialogBuilder.setTitle("Confirm delete")
            dialogBuilder.setMessage("Are you sure to delete this item")
            dialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, _ ->
                dbManager.removeColorFromTheWall(color.wallName, color.id)
                dialog.cancel()
            })
            dialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, _ ->
                dialog.cancel()
            })
            val alert = dialogBuilder.create()
            alert.show()
        }
    }
}