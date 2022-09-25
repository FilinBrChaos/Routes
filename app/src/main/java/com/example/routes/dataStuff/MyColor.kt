package com.example.routes.dataStuff

import android.graphics.Color

class MyColor(val wallName: String,
              val id: Int,
              var colorName: String,
              var colorValue: String,
              var isCheckedInLocalSettings: Boolean):Cloneable{
    companion object{
        fun parseFromString(dataString: String): MyColor{
            //remove ";" symbol
            var strings = dataString.substring(0, dataString.length - 1).split(",").toTypedArray()

            val wallName = strings[0].split(":").toTypedArray()[1]
            val id: Int = strings[1].split(":").toTypedArray()[1].toInt()
            val colorName = strings[2].split(":").toTypedArray()[1]
            val colorValue = strings[3].split(":").toTypedArray()[1]
            val isCheckedInLocalSettings = strings[4].split(":").toTypedArray()[1].toBoolean()
            return MyColor(wallName, id, colorName, colorValue, isCheckedInLocalSettings)
        }
    }

    public override fun clone(): Any {
        return MyColor(wallName, id, colorName, colorValue, isCheckedInLocalSettings)
    }

    fun getColor(): Int { return Color.parseColor(colorValue) }

    override fun toString(): String {
        return "[wallName]:$wallName, [id]:$id, [colorName]:$colorName, [colorValue]:$colorValue, [isCheckedInLocalSettings]:$isCheckedInLocalSettings;"
    }
}