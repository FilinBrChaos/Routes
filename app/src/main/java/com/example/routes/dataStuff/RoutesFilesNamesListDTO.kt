package com.example.routes.dataStuff

import kotlinx.serialization.Serializable

@Serializable
class RoutesFilesNamesListDTO {
    var routesFilesNames: ArrayList<String> = arrayListOf()
}