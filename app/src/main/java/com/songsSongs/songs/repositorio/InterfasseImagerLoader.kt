package com.songsSongs.songs.repositorio

import android.graphics.Bitmap
import android.net.Uri
import java.net.URI

interface InterfasseImagerLoader {
    suspend fun getBitmap(uri: Uri):Bitmap?
}