package com.example.songs.repositorio

import android.net.Uri

data class Album(val idDoalbum:Long,val nome:String,val artista:String,val uri:Uri)

data class Artista(val idDoArtista:Long,val nome:String)