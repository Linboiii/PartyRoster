package com.example.partyroster

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun loadBitmapFromResource(context: Context, resId: Int, maxSize: Int = 512): Bitmap? {
    return try {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeResource(context.resources, resId, options)

        var sample = 1
        while (options.outWidth / sample > maxSize || options.outHeight / sample > maxSize) {
            sample *= 2
        }

        val scaleOptions = BitmapFactory.Options().apply {
            inSampleSize = sample
        }
        BitmapFactory.decodeResource(context.resources, resId, scaleOptions)
    } catch (e: Exception) {
        null
    }
}