package com.example.routes.dataStuff

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbManager(context: Context?) : SQLiteOpenHelper(context, "app_data", null, 1) {

    var onDbChanged: () -> Unit = {}

    companion object{
        const val TABLE_WALL_A = "wall_a"
        const val TABLE_WALL_B = "wall_b"
        const val TABLE_WALL_C = "wall_c"

        private const val COLUMN_WALL_ID = "id"
        private const val COLUMN_WALL_COLOR_NAME = "color_name"
        private const val COLUMN_WALL_COLOR_VALUE = "color_value"

        private const val TABLE_ROUTES = "routes"
        private const val COLUMN_ROUTES_ID = "id"
        private const val COLUMN_ROUTES_ROUTE_NAME = "route_name"
        private const val COLUMN_ROUTES_WALL_NAME = "wall_name"
        private const val COLUMN_ROUTES_CREATION_DATE = "creation_date"
        private const val COLUMN_ROUTES_CREATOR = "creator"
        private const val COLUMN_ROUTES_ROUTE_COLORS = "routes_colors"
        private const val COLUMN_ROUTES_PICTURES_DATA = "pictures_data"
    }

    val wallName_A = TABLE_WALL_A
    val wallName_B = TABLE_WALL_B
    val wallName_C = TABLE_WALL_C

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_WALL_A(" +
                COLUMN_WALL_ID + " integer PRIMARY key AUTOINCREMENT, " +
                COLUMN_WALL_COLOR_NAME + " text, " +
                COLUMN_WALL_COLOR_VALUE + " text);")

        db?.execSQL("create table $TABLE_WALL_B(" +
                COLUMN_WALL_ID + " integer PRIMARY key AUTOINCREMENT, " +
                COLUMN_WALL_COLOR_NAME + " text, " +
                COLUMN_WALL_COLOR_VALUE + " text);")

        db?.execSQL("create table $TABLE_WALL_C(" +
                COLUMN_WALL_ID + " integer PRIMARY key AUTOINCREMENT, " +
                COLUMN_WALL_COLOR_NAME + " text, " +
                COLUMN_WALL_COLOR_VALUE + " text);")

        db?.execSQL("create table $TABLE_ROUTES(" +
                COLUMN_ROUTES_ID + " integer PRIMARY key AUTOINCREMENT, " +
                COLUMN_ROUTES_ROUTE_NAME + " text, " +
                COLUMN_ROUTES_WALL_NAME + " text, " +
                COLUMN_ROUTES_CREATION_DATE + " int, " +
                COLUMN_ROUTES_CREATOR + " text, " +
                COLUMN_ROUTES_ROUTE_COLORS + " text, " +
                COLUMN_ROUTES_PICTURES_DATA + " text);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //TODO make migration
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WALL_A")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WALL_B")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WALL_C")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTES")
        onCreate(db)
    }

    fun getWall(wallName: String): WallDTO{
        val db = this.readableDatabase
        val resultWall = WallDTO()
        val tableData: Cursor?
        var selectQuery = ""

        if (wallNameValidation(wallName)) { selectQuery = "SELECT * FROM $wallName"; resultWall.wallName = wallName }

        try { tableData = db.rawQuery(selectQuery, null) }
        catch (e: Exception) { e.printStackTrace(); throw e }

        if (tableData.moveToFirst()){
            do { resultWall.colorsOnTheWall.add(MyColor(resultWall.wallName, tableData.getInt(0), tableData.getString(1), tableData.getString(2))) }
            while (tableData.moveToNext())
        }
        return resultWall
    }

    fun addColorToWall(wallName: String, colorName: String, colorValue: String){
        val db = this.readableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_WALL_COLOR_NAME, colorName)
        contentValues.put(COLUMN_WALL_COLOR_VALUE, colorValue)

        var res: Long = 0
        if (wallNameValidation(wallName)) { res = db.insert(wallName, null, contentValues) }
        if (res == -1L) println("failed")
        onDbChanged()
    }

    fun removeColorFromTheWall(wallName: String, colorId: Int){
        val db = this.readableDatabase

        var res: Int = 0
        if (wallNameValidation(wallName)) { res = db.delete(wallName, COLUMN_WALL_ID + "=" + colorId, null) }
        if (res == -1) println("failed")
        onDbChanged()
    }

    private fun wallNameValidation(wallName: String): Boolean{
        return when (wallName){
            TABLE_WALL_A -> true
            TABLE_WALL_B -> true
            TABLE_WALL_C -> true
            TABLE_ROUTES -> true
            else -> throw Exception("Invalid wall name")
        }
    }

    fun addRouteRecord(routeData: RouteDTO){
        val db: SQLiteDatabase = this.readableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_ROUTES_ROUTE_NAME, routeData.routeName)
        contentValues.put(COLUMN_ROUTES_WALL_NAME, routeData.wallName)
        contentValues.put(COLUMN_ROUTES_CREATION_DATE, routeData.creationDate)
        contentValues.put(COLUMN_ROUTES_CREATOR, routeData.routeCreator)
        contentValues.put(COLUMN_ROUTES_ROUTE_COLORS, routeData.getRouteColorsInString())
        contentValues.put(COLUMN_ROUTES_PICTURES_DATA, routeData.picturesData)

        val res: Long = db.insert(TABLE_ROUTES, null, contentValues)
        if (res == -1L) println("failed")
    }

    @SuppressLint("Recycle")
    fun getAllRoutesRecords(): ArrayList<RouteDTO>{
        val db = this.readableDatabase
        val result: ArrayList<RouteDTO> = arrayListOf()
        val tableData: Cursor?
        val selectQuery = "SELECT * FROM $TABLE_ROUTES"

        try { tableData = db.rawQuery(selectQuery, null) }
        catch (e: Exception) { e.printStackTrace(); throw e }
        if (tableData.moveToFirst()){
            do {
                println("table data" + tableData.getString(0))
                result.add(
                    RouteDTO(tableData.getInt(0),
                    tableData.getString(1),
                    tableData.getString(2),
                    tableData.getString(3),
                    tableData.getString(4),
                    RouteDTO.parseColorsFromSolidJsonStr(tableData.getString(5)),
                    tableData.getString(6))
                )
            }
            while (tableData.moveToNext())
        }
        return result
    }

    fun removeRouteRecord(routeId: Int){
        val db: SQLiteDatabase = this.readableDatabase
        val res: Int = db.delete(TABLE_ROUTES, COLUMN_ROUTES_ID + "=" + routeId, null)
        if (res == -1) println("failed")
        onDbChanged()
    }
}