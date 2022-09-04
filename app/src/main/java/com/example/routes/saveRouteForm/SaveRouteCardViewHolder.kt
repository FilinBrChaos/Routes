package com.example.routes.saveRouteForm

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.colorPicker.ColorPickerActivity
import com.example.routes.databinding.ImageCardBinding

class SaveRouteCardViewHolder(private val cardBinding: ImageCardBinding,
                              private val resultLauncher: ActivityResultLauncher<Intent>): RecyclerView.ViewHolder(cardBinding.root) {

    fun bindCard(uri: Uri){
        cardBinding.image.setImageURI(uri)
        cardBinding.card.setOnClickListener {
            //pickImage()
            println("!!!!!!!!!! image clicked !!!!!!!!!!!")
        }
    }

    private fun checkPermission(activity: Activity){
        val permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){

        }
    }

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
}