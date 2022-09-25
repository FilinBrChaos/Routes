package com.example.routes.dataStuff

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.*

class ImageManager(val context: Context) {
    companion object Constants {
        const val IMAGE_FOLDER_NAME = "Routes"

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun createDirectoryAndSaveImage(imageToSave: Bitmap, fileName: String){

        var internalStorage: String = Environment.getExternalStorageDirectory().absolutePath
        var relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + IMAGE_FOLDER_NAME
        var fullFileName = "$fileName.jpg"
        //Insted of Environment.DIRECTORY_PICTURES this can be any valid directory like Environment.DIRECTORY_DCIM, Environment.DIRECTORY_DOWNLOADS etc..

        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fullFileName) //this is the file name you want to save
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg") // Content-Type
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)

        val resolver = context?.contentResolver

        var stream: OutputStream? = null
        var uri: Uri? = null

        try {
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            uri = resolver?.insert(contentUri, contentValues)

            if (uri == null) throw IOException("Failed to create new MediaStore record.")

            stream = resolver?.openOutputStream(uri)

            if (stream == null) throw IOException("Failed to get output stream.")

            if (!imageToSave.compress(Bitmap.CompressFormat.JPEG, 95, stream)) throw IOException("Failed to save bitmap.")
        } catch (e: IOException) {
            if (uri != null) {
                // Don't leave an open entry in the MediaStore
                resolver?.delete(uri, null, null)
            }
            throw e
        } finally { stream?.close() }
    }

    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.Q)
    fun getSavedImage(imageName: String): Bitmap{
        val path = IMAGE_FOLDER_NAME

        val selection = MediaStore.Files.FileColumns.RELATIVE_PATH + " like ? "

        val selectionArgs = arrayOf("%$path%")

        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.MediaColumns.TITLE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.MediaColumns.RELATIVE_PATH
        )
        val cursor = context.contentResolver.query(externalUri, projection, selection, selectionArgs,  MediaStore.Images.Media.DATE_TAKEN)
        val idColumn = cursor?.getColumnIndex(MediaStore.MediaColumns._ID)
        val titleColumn = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE)
        val relativePathColumn = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)
        val nameIndex = cursor?.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)

        while (cursor?.moveToNext()!!) {
            val photoUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getString(idColumn!!))
            //This will give you the uri of the images from that custom folder
            var fileName:String ? = null
            if (photoUri != null) {
                if (photoUri.toString().startsWith("file:")) {
                    fileName = photoUri.path
                } else {
                    val c = context.contentResolver.query(photoUri, null, null, null, null)
                    if (c != null && c.moveToFirst()) {
                        val id = c.getColumnIndex(MediaStore.Images.Media.DATA)
                        if (id != -1) {
                            fileName = c.getString(id)
                        }
                    }
                }
                Log.e("PHOTO", "Name is$fileName")
                Log.e("My name", imageName)
            }
//            Log.e("PHOTO", "URI is$photoUri")
            if (fileName != null) {
                if (fileName.contains(imageName)) {
                    val `is` = context.contentResolver.openInputStream(photoUri)
                    val bitmap = BitmapFactory.decodeStream(`is`)
        //            Log.e("Image Bitmap is","Here"+bitmap)
                    // Do whatever you want to do with your bitmaps
        //            myImage.setImageBitmap(bitmap)
                    return bitmap
                }
            }
        }
        throw Exception("There is no image with that name")
    }

    fun createUniqueName(): String{
        return UUID.randomUUID().toString()
    }

    fun convertUriToBitmap(uri: Uri): Bitmap{
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        else MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

        return bitmap
    }
}