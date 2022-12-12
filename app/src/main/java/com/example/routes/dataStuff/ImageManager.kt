package com.example.routes.dataStuff

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.*
import java.lang.Exception
import java.util.*

class ImageManager(val context: Context) {
    companion object Constants {
        const val IMAGE_FOLDER_NAME = "RoutesImages"
    }

    var external = false

    fun saveBitmap(image: Bitmap, imageName: String){
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(createFile(imageName))
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: Exception) { e.printStackTrace() }
        finally {
            try { fileOutputStream?.close() }
            catch (e: IOException) { e.printStackTrace() }
        }
    }

    private fun createFile(fileName: String): File{
        val directory: File
        if (external) {
            directory = getStorageDirectory(IMAGE_FOLDER_NAME)
            if (!directory.exists()) directory.mkdir()
        } else {
            directory = File(context.filesDir.absolutePath + File.separator + IMAGE_FOLDER_NAME)
            if (!directory.exists()) directory.mkdir()
        }
        return File(directory, fileName)
    }

    private fun getStorageDirectory(directoryName: String): File{
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), directoryName)
        if (!file.mkdirs()) Log.e("Image manager", "Directory not created")
        return file
    }

    fun isExternalStorageWritable(): Boolean{
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    fun isExternalStorageReadable(): Boolean{
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    fun loadImage(imageName: String): Bitmap? {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = FileInputStream(createFile(imageName))
            return BitmapFactory.decodeStream(fileInputStream)
        }
        catch (e: Exception) { e.printStackTrace() }
        finally {
            try {
                fileInputStream?.close()
            } catch (e: IOException){ e.printStackTrace() }
        }
        return null
    }

    fun deleteImage(imageName: String): Boolean{
        val file = createFile(imageName)
        return file.delete()
    }

    fun reduceImageQuality(){
        //this code reducing image quality in image card preview

//            val byteArrayOutputStream = ByteArrayOutputStream()
//            image.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//            val imageEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
//            val compressedImage: Bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)


        //binding.image.setImageBitmap(compressedImage)
    }

    fun convertUriToBitmap(uri: Uri): Bitmap {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        else MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }

    fun createUniqueName(): String{
        return UUID.randomUUID().toString()
    }
}