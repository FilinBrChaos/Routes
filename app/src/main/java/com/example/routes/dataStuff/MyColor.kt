package com.example.routes.dataStuff

import android.graphics.Color

class MyColor(val wallName: String, val id: Int, var colorName: String, var colorValue: String):Cloneable{
    public override fun clone(): Any {
        return MyColor(wallName, id, colorName, colorValue)
    }

    fun getColor(): Int { return Color.parseColor(colorValue) }
}