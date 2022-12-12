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

        private const val TABLE_ROUTES_IMAGES = "routes_images"
        private const val COLUMN_ROUTE_IMAGE_ID = "id"
        private const val COLUMN_ROUTE_IMAGE_NAME = "name"
        private const val COLUMN_ROUTE_IMAGE_PARENT_ROUTE = "parent_route"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLE_WALLS(" +
                "$COLUMN_WALL_ID integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_WALL_NAME text);")

        for (wall in WALLS_NAMES){
            val contentValues = ContentValues()
            contentValues.put(COLUMN_WALL_NAME, wall)
            db!!.insert(TABLE_WALLS, null, contentValues)
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
                "$COLUMN_ROUTE_IMAGE_NAME text, " +
                "$COLUMN_ROUTE_IMAGE_PARENT_ROUTE integer, " +
                "foreign key ($COLUMN_ROUTE_IMAGE_PARENT_ROUTE) references $TABLE_ROUTES($COLUMN_ROUTE_ID) " +
                "on delete cascade" +
                ");")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WALLS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COLORS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTES_COLORS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ROUTES_IMAGES")
        onCreate(db)
    }


    fun saveRoute(route: RouteDTO):Boolean {
        val db = this.readableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_ROUTE_NAME, route.routeName)
        contentValues.put(COLUMN_ROUTE_USED_WALLS, route.wallName)
        contentValues.put(COLUMN_ROUTE_CREATION_DATE, route.creationDate)
        contentValues.put(COLUMN_ROUTE_CREATOR, route.routeCreator)

        var res: Long = db.insert(TABLE_ROUTES, null, contentValues)

        var routeId = 0
        val cursor = db.rawQuery("select $COLUMN_ROUTE_ID from $TABLE_ROUTES where $COLUMN_ROUTE_ID = (select max($COLUMN_ROUTE_ID) from $TABLE_ROUTES)", null)
        if (cursor.moveToFirst()) routeId = cursor.getInt(0)
        else return false

        for (color in route.routeColors){
            val colorsOnRoute = ContentValues()
            colorsOnRoute.put(COLUMN_ROUTE_COLOR_NAME, color.colorName)
            colorsOnRoute.put(COLUMN_ROUTE_COLOR_VALUE, color.colorValue)
            colorsOnRoute.put(COLUMN_ROUTE_COLOR_PARENT_ROUTE, routeId)

            db.insert(TABLE_ROUTES_COLORS, null, colorsOnRoute)
        }

        for (image in route.picturesData){
            val routeImages = ContentValues()
            routeImages.put(COLUMN_ROUTE_IMAGE_NAME, image)
            routeImages.put(COLUMN_ROUTE_IMAGE_PARENT_ROUTE, routeId)

            db.insert(TABLE_ROUTES_IMAGES, null, routeImages)
        }

        db.close()
        onDbChanged()
        return res > 0
    }

    fun getRoute(routeId: Int): RouteDTO {
        val db = this.readableDatabase
        val routeRaw: Cursor?
        var resultRoute = RouteDTO()
        val routeColorsRaw: Cursor?
        val routeColors: ArrayList<MyColor> = arrayListOf()
        val routeImagesRaw: Cursor?
        val routeImages: ArrayList<String> = arrayListOf()
        var selectQuery = ""

        selectQuery = "select * " +
                "from $TABLE_ROUTES_COLORS " +
                "where $COLUMN_ROUTE_COLOR_PARENT_ROUTE = $routeId"

        try { routeColorsRaw = db.rawQuery(selectQuery, null) }
        catch (e: Exception) { e.printStackTrace(); throw e }

        if (routeColorsRaw.moveToFirst()){
            do { routeColors.add(MyColor("", routeColorsRaw.getString(1), routeColorsRaw.getString(2), false, routeColorsRaw.getInt(0)))
            } while (routeColorsRaw.moveToNext())
        }

        selectQuery = "select * " +
                "from $TABLE_ROUTES_IMAGES " +
                "where $COLUMN_ROUTE_IMAGE_PARENT_ROUTE = $routeId"

        try { routeImagesRaw = db.rawQuery(selectQuery, null) }
        catch (e: Exception) { e.printStackTrace(); throw e }

        if (routeImagesRaw.moveToFirst()){
            do { routeImages.add(routeImagesRaw.getString(1))
            } while (routeImagesRaw.moveToNext())
        }

        selectQuery = "SELECT * " +
                "FROM $TABLE_ROUTES " +
                "WHERE $COLUMN_ROUTE_ID = $routeId "

        try { routeRaw = db.rawQuery(selectQuery, null) }
        catch (e: Exception) { e.printStackTrace(); throw e }

        if (routeRaw.moveToFirst()){
            resultRoute = RouteDTO(routeRaw.getString(1),
                                   routeRaw.getString(2),
                                routeRaw.getString(3),
                                routeRaw.getString(4),
                                routeColors, routeImages, routeId)
        }
        return resultRoute
    }

    fun getAllRoutes(): ArrayList<RouteDTO>{
        val db = this.readableDatabase
        val routesIdsRaw: Cursor?
        val routesIds: ArrayList<Int> = arrayListOf()
        var resultRoutes: ArrayList<RouteDTO> = arrayListOf()
        var selectQuery = ""

        selectQuery = "select $COLUMN_ROUTE_ID " +
                "from $TABLE_ROUTES;"

        try { routesIdsRaw = db.rawQuery(selectQuery, null) }
        catch (e: Exception) { e.printStackTrace(); throw e }

        if (routesIdsRaw.moveToFirst()){
            do { routesIds.add(routesIdsRaw.getInt(0))
            } while (routesIdsRaw.moveToNext())
        }

        for (id in routesIds) resultRoutes.add(getRoute(id))
        return resultRoutes
    }

    fun deleteRoute(): Boolean{
        return true
    }

    fun addColorToWall(color: MyColor): Boolean{
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
        return res > 0
    }

    fun removeColorFromTheWall(colorId: Int): Boolean{
        val db = this.readableDatabase

        var res: Int = db.delete(TABLE_COLORS,
            "$COLUMN_COLOR_ID=$colorId", null)
        onDbChanged()
        return res > 0
    }

    fun updateColorOnTheWall(color: MyColor): Boolean{
        val db = this.readableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_COLOR_NAME, color.colorName)
        contentValues.put(COLUMN_COLOR_VALUE, color.colorValue)
        contentValues.put(COLUMN_COLOR_CHECKED, if (color.isCheckedInLocalSettings) 1 else 0)

        var res: Int = db.update(TABLE_COLORS, contentValues, "$COLUMN_COLOR_ID = ${color.id}", null)
        db.close()
        onDbChanged()
        return res > 0
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
                    "W.$COLUMN_WALL_NAME = '$wallName'";
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