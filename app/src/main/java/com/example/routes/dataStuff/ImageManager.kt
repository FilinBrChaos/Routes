package com.example.routes.dataStuff

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target.*

class ImageManager {
    companion object {
        fun drawInImageView(applicationContext: Context, imageUrl: String, targetImageView: ImageView) {
            Glide.with(applicationContext)
                .load(imageUrl)
                .apply(RequestOptions()
                    .override(SIZE_ORIGINAL))
                .into(targetImageView)
        }

        fun drawInImageView(applicationContext: Context, drawableId: Int, targetImageView: ImageView) {
            Glide.with(applicationContext)
                .load(drawableId)
                .apply(RequestOptions()
                    .override(SIZE_ORIGINAL))
                .into(targetImageView)
        }

        fun drawInImageViewCircleCrop(applicationContext: Context, imageUrl: String, targetImageView: ImageView) {
            Glide.with(applicationContext)
                .load(imageUrl)
                .apply(RequestOptions()
                    .override(SIZE_ORIGINAL))
                .circleCrop()
                .into(targetImageView)
        }

        fun drawInImageViewCircleCrop(applicationContext: Context, drawableId: Int, targetImageView: ImageView) {
            Glide.with(applicationContext)
                .load(drawableId)
                .apply(RequestOptions()
                    .override(SIZE_ORIGINAL))
                .circleCrop()
                .into(targetImageView)
        }
    }
}