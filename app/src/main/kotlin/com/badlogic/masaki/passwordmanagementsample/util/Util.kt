package com.badlogic.masaki.passwordmanagementsample.util

import android.content.Context
import android.content.res.Configuration
import android.os.Environment
import android.util.Log
import android.graphics.Bitmap
import android.content.ContextWrapper
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import com.badlogic.masaki.passwordmanagementsample.BuildConfig
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.io.*


/**
 * Created by masaki on 2017/10/25.
 */

fun saveToInternalStorage(context: Context, bitmapImage: Bitmap?): String {
    if (bitmapImage === null) {
        return ""
    }
    val cw = ContextWrapper(context)
    val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
    // Create imageDir
    val path = File(directory, SimpleDateFormat("yyyyMMddHHmmss").format(Date()) + ".png")

    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(path)
        // Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage?.compress(Bitmap.CompressFormat.PNG, 100, fos)
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    } finally {
        try {
            fos!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    return path.absolutePath
}

fun getSingleOfSavingImageToInternal(context: Context, bitmapImage: Bitmap?): Single<String> {
    val emitter: ((e: SingleEmitter<String>) -> Unit) = { emitter: SingleEmitter<String> ->
        try {
            val absolutePath: String = saveToInternalStorage(context, bitmapImage)
            emitter.onSuccess(absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            emitter.onError(e)
        }
    }
    return Single.create(emitter)
}

fun loadImageFromStorage(path: String): Bitmap {
    return try {
        BitmapFactory.decodeFile(path)
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}

fun LogWrapperD(tag: String, msg: String) {
    when (BuildConfig.DEBUG) {
        true -> Log.d(tag, msg)
        else -> return
    }
}

private object DateUtils {
    val df = SimpleDateFormat("yyyy/MM/dd")
}

fun dateFormat(date: Date?): String {
    return if (date != null) {
        DateUtils.df.format(date)
    } else {
        ""
    }
}


