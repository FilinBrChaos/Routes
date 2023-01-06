package com.example.routes

import com.example.routes.dataStuff.MyColor

object AppRuntimeData {
    init { /*this object creates DON'T DELETE THIS*/ }
    var currentGeneratedRouteColors: ArrayList<MyColor> = arrayListOf()
}