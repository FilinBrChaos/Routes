package com.example.routes.cardsStuff

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.routes.databinding.ImageCardBinding
import java.io.ByteArrayOutputStream

class ImageCardViewHolder(val binding: ImageCardBinding) {
    fun bindCard(image: Bitmap, onCardClickHandler: (image: Bitmap) -> Unit){
        binding.image.setImageBitmap(image)
        binding.card.setOnClickListener {
            onCardClickHandler(image)
        }
    }
}