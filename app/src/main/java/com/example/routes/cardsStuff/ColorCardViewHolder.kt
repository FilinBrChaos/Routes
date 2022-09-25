package com.example.routes.cardsStuff

import android.graphics.Color
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.ColorCardBinding

class ColorCardViewHolder(val binding: ColorCardBinding) {
    fun bindCard(color: MyColor){
        binding.colorPreviewBlock.setBackgroundColor(Color.parseColor(color.colorValue))
        binding.colorName.text = color.colorName
    }
}