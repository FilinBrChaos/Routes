package com.example.routes

import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.dataStuff.RouteDTO

object AppRuntimeData {
    init {
        println("Singleton was started")

    }
    var routeDTOTemporary: RouteDTO? = null

    var globalDbManager: DbManager? = null

    var colorsListInLocalSettings: ArrayList<MyColor> = arrayListOf()

    var currentGeneratedRouteColors: ArrayList<MyColor> = arrayListOf()
}