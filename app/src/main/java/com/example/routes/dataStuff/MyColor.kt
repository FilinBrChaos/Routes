package com.example.routes.dataStuff

import android.graphics.Color

class MyColor(val wallName: String, val id: Int, var colorName: String, var colorValue: String):Cloneable{
    companion object{
        fun parseFromString(dataString: String): MyColor{
            val strings = dataString.split(", ").toTypedArray()
            val wallName = strings[0].split(":").toTypedArray()[1]
            val id: Int = strings[1].split(":").toTypedArray()[1].toInt()
            val colorName = strings[2].split(":").toTypedArray()[1]
            val colorValueRaw = strings[3].split(":").toTypedArray()[1]
            val colorValue = colorValueRaw.substring(0, colorValueRaw.length - 1)
            return MyColor(wallName, id, colorName, colorValue)
        }
    }

    public override fun clone(): Any {
        return MyColor(wallName, id, colorName, colorValue)
    }

    fun getColor(): Int { return Color.parseColor(colorValue) }

    override fun toString(): String {
        return "[wallName]:" + wallName + ", [id]:" + id + ", [colorName]:" + colorName + ", [colorValue]:" + colorValue + ";"
    }
}