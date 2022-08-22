package com.example.routes

import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor

object AppRuntimeData {
    init {
        println("Singleton was started")

    }
    var globalDbManager: DbManager? = null

    var colorsListInLocalSettings: ArrayList<MyColor> = arrayListOf()

    var generatedColorsInColorsView = ""
}