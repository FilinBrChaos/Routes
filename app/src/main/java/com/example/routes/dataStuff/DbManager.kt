package com.example.routes.dataStuff

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbManager(context: Context?) : SQLiteOpenHelper(context, "app_data", null, 1) {

    var onDbChanged: () -> Unit = {}
    var wallsNames = WALLS_NAMES

    companion object{
        const val TABLE_WALLS = "wall"
        private const val COLUMN_WALL_ID = "id"
        private const val COLUMN_WALL_NAME = "name"
        val WALLS_NAMES = arrayListOf("wallA", "wallB", "wallC")

        private const val TABLE_COLORS = "colors"
        private const val COLUMN_COLOR_ID = "id"
        private const val COLUMN_COLOR_NAME = "name"
        private const val COLUMN_COLOR_VALUE = "value"
        private const val COLUMN_COLOR_CHECKED = "checked"
        private const val COLUMN_COLOR_PARENT_WALL = "parent_wall"
        private const val COLUMN_COLOR_PARENT_WALL_FK = "parent_wall_fk"

        private const val TABLE_ROUTES = "routes"
        private const val COLUMN_ROUTE_ID = "id"
        private const val COLUMN_ROUTE_NAME = "name"
        private const val COLUMN_ROUTE_USED_WALLS = "used_walls"
        private const val COLUMN_ROUTE_CREATION_DATE = "creation_date"
        private const val COLUMN_ROUTE_CREATOR = "creator"

        private const val TABLE_ROUTES_COLORS = "routes_colors"
        private const val COLUMN_ROUTE_COLOR_ID = "id"
        private const val COLUMN_ROUTE_COLOR_NAME = "name"
        private const val COLUMN_ROUTE_COLOR_VALUE = "value"
        private const val COLUMN_ROUTE_COLOR_PARENT_ROUTE = "parent_route"
        private const val COLUMN_ROUTE_COLOR_PARENT_ROUTE_FK = "parent_route_fk"

        private const val TABLE_ROUTES_IMAGES = "routes_images"
        private const val COLUMN_ROUTE_IMAGE_ID = "id"
        private const val COLUMN_ROUTE_IMAGE_PATH = "path"
        private const val COLUMN_ROUTE_IMAGE_PARENT_ROUTE = "parent_route"
        private const val COLUMN_ROUTE_IMAGE_PARENT_ROUTE_FK = "parent_route_fk"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_WALLS(" +
                "$COLUMN_WALL_ID integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_WALL_NAME text);")

        for (wall in WALLS_NAMES){
            val contentValues = ContentValues()
            contentValues.put(COLUMN_WALL_NAME, wall)
            var res: Long = db!!.insert(TABLE_WALLS, null, contentValues)
        }

        db?.execSQL("create table $TABLE_COLORS(" +
                "$COLUMN_COLOR_ID integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_COLOR_NAME text, " +
                "$COLUMN_COLOR_VALUE text, " +
                "$COLUMN_COLOR_CHECKED integer, " +
                "$COLUMN_COLOR_PARENT_WALL integer, " +
                "foreign key ($COLUMN_COLOR_PARENT_WALL) references $TABLE_WALLS($COLUMN_WALL_ID)" +
                ");")

        db?.execSQL("create table $TABLE_ROUTES(" +
                "$COLUMN_ROUTE_ID integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ROUTE_NAME text, " +
                "$COLUMN_ROUTE_USED_WALLS text, " +
                "$COLUMN_ROUTE_CREATION_DATE text, " +
                "$COLUMN_ROUTE_CREATOR text" +
                ");")

        db?.execSQL("create table $TABLE_ROUTES_COLORS(" +
                "$COLUMN_ROUTE_COLOR_ID integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ROUTE_COLOR_NAME text, " +
                "$COLUMN_ROUTE_COLOR_VALUE text, " +
                "$COLUMN_ROUTE_COLOR_PARENT_ROUTE integer, " +
                "foreign key ($COLUMN_ROUTE_COLOR_PARENT_ROUTE) references $TABLE_ROUTES($COLUMN_ROUTE_ID) " +
                "on delete cascade" +
                ");")

        db?.execSQL("create table $TABLE_ROUTES_IMAGES(" +
                "$COLUMN_ROUTE_IMAGE_ID integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ROUTE_IMAGE_PATH text, " +
                "$COLUMN_ROUTE_IMAGE_PARENT_ROUTE integer, " +
                "foreign key ($COLUMN_ROUTE_IMAGE_PARENT_ROUTE) references $TABLE_ROUTES($COLUMN_ROUTE_ID) " +
                "on delete cascade" +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //TODO make migration
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WALLS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COLORS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTES_COLORS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTES_IMAGES")
        onCreate(db)
    }

    fun getWall(wallName: String): WallDTO{
        val db = this.readableDatabase
        val resultWall = WallDTO()
        val tableData: Cursor?
        var selectQuery = ""

        if (wallNameValidation(wallName)) {
            selectQuery = "SELECT * " +
                    "FROM $TABLE_COLORS C, $TABLE_WALLS W " +
                    "WHERE C.$COLUMN_COLOR_PARENT_WALL = W.$COLUMN_WALL_ID AND " +
                    "W.$COLUMN_WALL_ID = (SELECT $COLUMN_WALL_ID FROM $TABLE_WALLS WHERE $COLUMN_WALL_NAME = '$wallName') limit 1";
            resultWall.wallName = wallName
        } else { throw Exception("Wall name don't existed") }

        try { tableData = db.rawQuery(selectQuery, null) }
        catch (e: Exception) { e.printStackTrace(); throw e }

        if (tableData.moveToFirst()){
            do {
                val checked: Boolean = tableData.getInt(3) != 0
                resultWall.colorsOnTheWall.add(MyColor(resultWall.wallName, tableData.getString(1), tableData.getString(2), checked, tableData.getInt(0))) }
            while (tableData.moveToNext())
        }
        return resultWall
    }

    fun saveRoute():Boolean {
        return true
    }

    fun getRoute(): RouteDTO {
        return RouteDTO()
    }
//todo this
    fun deleteRoute(): Boolean{
        return true
    }

    fun addColorToWall(color: MyColor){
        val db = this.readableDatabase
        var wallId = 0
        val cursor = db.rawQuery("select id from $TABLE_WALLS where $COLUMN_WALL_NAME = '${color.wallName}'", null)
        if (cursor.moveToFirst()) wallId = cursor.getInt(0)

        val contentValues = ContentValues()
        contentValues.put(COLUMN_COLOR_NAME, color.colorName)
        contentValues.put(COLUMN_COLOR_VALUE, color.colorValue)
        contentValues.put(COLUMN_COLOR_CHECKED, if (color.isCheckedInLocalSettings) 1 else 0)
        contentValues.put(COLUMN_COLOR_PARENT_WALL, wallId)

        var res: Long = db.insert(TABLE_COLORS, null, contentValues)
        db.close()
        onDbChanged()
    }

    fun removeColorFromTheWall(colorId: Int){
        val db = this.readableDatabase

        var res: Int = db.delete(TABLE_COLORS,
            "$COLUMN_COLOR_ID=$colorId", null)
        if (res == -1) println("failed")
        onDbChanged()
    }

//    fun updateColorOnTheWall(color: MyColor){
//        val db = this.readableDatabase
//
//        if (tableNameValidation(color.wallName)) db.execSQL("UPDATE " + color.wallName +
//                " SET " + COLUMN_WALL_COLOR_NAME + " = '" + color.colorName + "', " +
//                COLUMN_WALL_COLOR_VALUE + " = '" + color.colorValue + "' " +
//                "WHERE " + COLUMN_WALL_ID + " = '" + color.id + "';")
//    }

    private fun wallNameValidation(wallName: String): Boolean{
        return WALLS_NAMES.contains(wallName)
    }

//    fun addRouteRecord(routeData: RouteDTO){
//        val db: SQLiteDatabase = this.readableDatabase
//
//        val contentValues = ContentValues()
//        contentValues.put(COLUMN_ROUTES_ROUTE_NAME, routeData.routeName)
//        contentValues.put(COLUMN_ROUTES_WALL_NAME, routeData.wallName)
//        contentValues.put(COLUMN_ROUTES_CREATION_DATE, routeData.creationDate)
//        contentValues.put(COLUMN_ROUTES_CREATOR, routeData.routeCreator)
//        contentValues.put(COLUMN_ROUTES_ROUTE_COLORS, routeData.getRouteColorsInString())
//        contentValues.put(COLUMN_ROUTES_PICTURES_DATA, routeData.picturesData)
//
//        val res: Long = db.insert(TABLE_ROUTES, null, contentValues)
//        if (res == -1L) println("failed")
//    }

//    @SuppressLint("Recycle")
//    fun getAllRoutesRecords(): ArrayList<RouteDTO>{
//        val db = this.readableDatabase
//        val result: ArrayList<RouteDTO> = arrayListOf()
//        val tableData: Cursor?
//        val selectQuery = "SELECT * FROM $TABLE_ROUTES"
//
//        try { tableData = db.rawQuery(selectQuery, null) }
//        catch (e: Exception) { e.printStackTrace(); throw e }
//        if (tableData.moveToFirst()){
//            do {
//                //println("table data" + tableData.getString(0))
//                result.add(
//                    RouteDTO(tableData.getInt(0),
//                    tableData.getString(1),
//                    tableData.getString(2),
//                    tableData.getString(3),
//                    tableData.getString(4),
//                    RouteDTO.parseColorsFromSolidJsonStr(tableData.getString(5)),
//                    tableData.getString(6))
//                )
//            }
//            while (tableData.moveToNext())
//        }
//        return result
//    }

    fun removeRouteRecord(routeId: Int){
//        val db: SQLiteDatabase = this.readableDatabase
//        val res: Int = db.delete(TABLE_ROUTES, COLUMN_ROUTES_ID + "=" + routeId, null)
//        if (res == -1) println("failed")
        onDbChanged()
    }

    fun deleteFrom(tableName: String){
//        if (tableNameValidation(tableName)){
//            this.readableDatabase.execSQL("delete from $tableName")
//        }
    }
}