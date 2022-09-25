package com.example.routes.cardsStuff

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.routes.databinding.ImageCardBinding
import java.io.ByteArrayOutputStream

class ImageCardViewHolder(val binding: ImageCardBinding) {
    fun bindCard(image: Bitmap){
        if (image != null){
            //this code reducing image quality in image card preview

//            val byteArrayOutputStream = ByteArrayOutputStream()
//            image.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//            val imageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
//            val compressedImage: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)


            //binding.image.setImageBitmap(compressedImage)
            binding.image.setImageBitmap(image)
        }
        binding.card.setOnClickListener {
            //TODO make image editing here
        }
    }
}