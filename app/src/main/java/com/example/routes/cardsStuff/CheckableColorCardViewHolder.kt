package com.example.routes.cardsStuff

import com.example.routes.*
import android.graphics.Color
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.CheckableColorCardBinding

class CheckableColorCardViewHolder(val binding: CheckableColorCardBinding) {
    fun bindCard(color: MyColor, checkChangedHandler: (color: MyColor) -> Unit) {
        binding.colorPreview.setBackgroundColor(Color.parseColor(color.colorValue))
        binding.colorNameText.text = color.colorName
        setCheckedBox(binding, color.isCheckedInLocalSettings)

        binding.checkBoxClickArea.setOnClickListener {
            checkChangedHandler(color)
            setCheckedBox(binding, color.isCheckedInLocalSettings)
        }
    }

    private fun setCheckedBox(binding: CheckableColorCardBinding, isChecked: Boolean){
        if (isChecked)
            binding.checkIcon.setBackgroundResource(R.drawable.ic_check_box_checked)
        else
            binding.checkIcon.setBackgroundResource(R.drawable.ic_check_box_blank)
    }
}