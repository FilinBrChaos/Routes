package com.example.routes.dataStuff

import android.util.Log
import androidx.fragment.app.FragmentActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

//class DataManager(val context: FragmentActivity?) {
//    companion object Constants{
//        private const val APP_DATA_DIR_NAME = "AppData"
//        private const val ROUTES_DIR_NAME = "Routes"
//        private const val WALLS_DIR_NAME = "Walls"
//
//        private const val ROUTES_LIST_COORDINATION_FILE_NAME = "routes_list"
//        const val WALL_A_NAME = "wall_a"
//        const val WALL_B_NAME = "wall_b"
//        const val WALL_C_NAME = "wall_c"
//    }
//
//    //var onDataChanged: () -> Unit = {}
//
//    fun saveWall(wallObj: WallDTO): Boolean{
//        if (wallNameValidation(wallObj.wallName)){
//            val serial = Json.encodeToString(wallObj)
//            val file = createFile(WALLS_DIR_NAME, wallObj.wallName)
//            return saveJsonToFile(serial, file)
//        }
//        else { Log.e("DataManager", "There was no wall with name [${wallObj.wallName}]"); }
//        return false
//    }
//
//    fun getWall(wallName: String): WallDTO?{
//        if (wallNameValidation(wallName)){
//            val file = createFile(WALLS_DIR_NAME, wallName)
//            val jsonResult = readJsonFromFile(file)
//            if (jsonResult.isEmpty()) return WallDTO()
//            return Json.decodeFromString(jsonResult)
//        }
//        return null
//    }
//
//    fun saveRoute(routeObj: RouteDTO): Boolean{
//        val routeFileName = createUniqueName()
//        routeObj.fileName = routeFileName
//        val routesList = getRoutesList()
//        val file = createFile(ROUTES_DIR_NAME, routeFileName)
//        val json = Json.encodeToString(routeObj)
//        return if (saveJsonToFile(json, file)){
//            routesList.routesFilesNames.add(routeFileName)
//            saveJsonToFile(Json.encodeToString(routesList), createFile(ROUTES_DIR_NAME, ROUTES_LIST_COORDINATION_FILE_NAME))
//            true
//        } else false
//    }
//
//    fun getAllRoutes(): ArrayList<RouteDTO>{
//        val routes = arrayListOf<RouteDTO>()
//        val routesList = getRoutesList()
//        for (routeName in routesList.routesFilesNames)
//            routes.add(getRoute(routeName))
//        return routes
//    }
//
//    fun getRoute(fileName: String): RouteDTO{
//        val routeFile = getFileIfExists(ROUTES_DIR_NAME, fileName) ?: return RouteDTO()
//        val jsonResult = readJsonFromFile(routeFile)
//        if (jsonResult.isEmpty()) return RouteDTO()
//        return Json.decodeFromString(jsonResult)
//    }
//
//    fun deleteRoute(fileName: String): Boolean{
//        val file = getFileIfExists(ROUTES_DIR_NAME, fileName)
//        if (file != null) {
//            val route = getRoute(fileName)
//            val imageManager = ImageManager(context!!)
//            for (image in route.picturesData){
//                imageManager.deleteImage(image)
//            }
//        }
//        return file?.delete() ?: false
//    }
//
//    fun getRoutesList(): RoutesFilesNamesListDTO {
//        val routesListFile = createFile(ROUTES_DIR_NAME, ROUTES_LIST_COORDINATION_FILE_NAME)
//        val jsonResult = readJsonFromFile(routesListFile)
//        return try {
//            if (jsonResult.isEmpty()) return RoutesFilesNamesListDTO()
//            Json.decodeFromString(jsonResult)
//        } catch (e: java.lang.Exception) { e.printStackTrace(); RoutesFilesNamesListDTO() }
//    }
//
//    private fun saveJsonToFile(json: String, file: File): Boolean{
//        return try {
//            val fileWriter = FileWriter(file)
//            val bufferedWriter = BufferedWriter(fileWriter)
//            bufferedWriter.write(json)
//            bufferedWriter.close()
//            true
//        } catch (e: java.lang.Exception) { e.printStackTrace(); false }
//    }
//
//    private fun readJsonFromFile(file: File): String{
//        return try {
//            val fileReader = FileReader(file)
//            val bufferedReader = BufferedReader(fileReader)
//            val jsonResult = StringBuilder()
//            var stringLineFromFile = bufferedReader.readLine()
//            while (stringLineFromFile != null) {
//                jsonResult.append(stringLineFromFile)
//                stringLineFromFile = bufferedReader.readLine()
//            }
//            bufferedReader.close()
//            jsonResult.toString()
//        }catch (e: Exception) { e.printStackTrace(); "" }
//    }
//
//    private fun wallNameValidation(wallName: String): Boolean{
//        return when (wallName){
//            WALL_A_NAME -> true
//            WALL_B_NAME -> true
//            WALL_C_NAME -> true
//            else -> throw Exception("Invalid wall name")
//        }
//    }
//
//    private fun createFile(dirName: String, fileName: String): File {
//        val directory = File(context!!.filesDir.absolutePath + File.separator + dirName)
//        if (!directory.exists()) directory.mkdir()
//        return File(directory, fileName)
//    }
//
//    private fun getFileIfExists(dirName: String, fileName: String): File? {
//        val directory = File(context!!.filesDir.absolutePath + File.separator + dirName)
//        return if (!directory.exists()) null
//        else File(directory, fileName)
//    }
//
//    fun createUniqueName(): String{
//        return UUID.randomUUID().toString()
//    }
//}