package com.example.routes.dataStuff

import android.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
class MyColor(val wallName: String = "blank",
              var colorName: String = "black",
              var colorValue: String = "#000000",
              var isCheckedInLocalSettings: Boolean = false,
              var id: Int = 0):Cloneable{

    public override fun clone(): Any {
        return MyColor(wallName, colorName, colorValue, isCheckedInLocalSettings)
    }

    fun getColor(): Int { return Color.parseColor(colorValue) }
}