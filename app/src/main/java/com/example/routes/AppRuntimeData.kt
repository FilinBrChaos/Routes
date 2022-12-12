package com.example.routes

import com.example.routes.dataStuff.MyColor

object AppRuntimeData {
    init {
        println("AppRuntimeData was started")
    }
    var currentGeneratedRouteColors: ArrayList<MyColor> = arrayListOf()
}