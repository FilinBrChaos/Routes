package com.example.routes.dataStuff

import kotlinx.serialization.Serializable

@Serializable
data class WallDTO(var wallName: String = "empty", var colorsOnTheWall: ArrayList<MyColor> = arrayListOf())