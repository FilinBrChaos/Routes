package com.example.routes.dataStuff

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext

class RouteDTO(val id: Int,
               val routeName: String,
               val wallName: String,
               val creationDate: String,
               val routeCreator: String,
               val routeColors: ArrayList<MyColor>,
               val picturesData: String) {
    companion object{
        fun parseColorsFromSolidJsonStr(jsonString: String): ArrayList<MyColor>{
            val jsonArr = jsonString.split("/")
            val colorList: ArrayList<MyColor> = arrayListOf()
            for (colorInStr in jsonArr)
                if (colorInStr != "")
                    colorList.add(MyColor.parseFromString(colorInStr))
            return colorList
        }
    }

    fun getRouteColorsInString(): String{
        val result = StringBuilder()
        for (color in routeColors) result.append(color.toString() + "/")
        return result.toString()
    }
}