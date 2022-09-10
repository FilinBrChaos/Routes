package com.example.routes.globalSettings

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.ColorCardBinding

class GlobalSettingsCardViewHolder(private val colorCardBinding: ColorCardBinding) : RecyclerView.ViewHolder(colorCardBinding.root) {
    fun bindColor(callerActivity: Activity, color: MyColor, position: Int, dbManager: DbManager){
        colorCardBinding.colorPreview.setBackgroundColor(Color.parseColor(color.colorValue))
        //if (position == 0) colorCardBinding.separatedLine.setBackgroundColor(colorCardBinding.root.solidColor)
        colorCardBinding.colorNameText.text = color.colorName
        colorCardBinding.deleteButton.setOnClickListener {
            var dialogBuilder = AlertDialog.Builder(callerActivity)
            dialogBuilder.setTitle("Confirm delete")
            dialogBuilder.setMessage("Are you sure to delete this item")
            dialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, _ ->
                dbManager.removeColorFromTheWall(color.wallName, color.id)
                dialog.cancel()
            })
            dialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, _ ->
                dialog.cancel()
            })
            var alert = dialogBuilder.create()
            alert.show()

            //updateRecyclerView()
        }

    }
}