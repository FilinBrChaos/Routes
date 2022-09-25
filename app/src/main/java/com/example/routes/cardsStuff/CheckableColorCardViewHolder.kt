package com.example.routes.cardsStuff

import com.example.routes.*
import android.graphics.Color
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.CheckableColorCardBinding

class CheckableColorCardViewHolder(val binding: CheckableColorCardBinding) {
    fun bindCard(color: MyColor) {
        binding.colorPreview.setBackgroundColor(Color.parseColor(color.colorValue))
        binding.colorNameText.text = color.colorName
        if (color.isCheckedInLocalSettings)
            binding.checkIcon.setBackgroundResource(R.drawable.ic_check_box_checked)
        else
            binding.checkIcon.setBackgroundResource(R.drawable.ic_check_box_blank)

        binding.checkBoxClickArea.setOnClickListener {
            println(color.isCheckedInLocalSettings.toString())
            if (color.isCheckedInLocalSettings){
                color.isCheckedInLocalSettings = false
                binding.checkIcon.setBackgroundResource(R.drawable.ic_check_box_blank)
            }
            else{
                color.isCheckedInLocalSettings = true
                binding.checkIcon.setBackgroundResource(R.drawable.ic_check_box_checked)
            }
        }
    }
}