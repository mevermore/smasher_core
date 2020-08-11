package com.smasher.core.io

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore

/**
 * @author Smasher
 * on 2020/4/24 0024
 */
class FileUtilHelper {


    fun aaa(context: Context) {
        var contentResolver: ContentResolver = context.contentResolver
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                //println("image uri is $uri")
            }
            cursor.close()
        }
    }


}