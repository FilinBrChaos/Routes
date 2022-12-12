package com.example.routes.dataStuff

import kotlinx.serialization.Serializable

@Serializable
class RouteDTO(val routeName: String = "---",
               val wallName: String = "---",
               val creationDate: String = "---",
               val routeCreator: String = "---",
               val routeColors: ArrayList<MyColor> = arrayListOf(),
               val picturesData: ArrayList<String> = arrayListOf(),
               var id: Int = 0){
    fun isEmpty(): Boolean{
        return wallName == "---" && creationDate == "---"
    }
}