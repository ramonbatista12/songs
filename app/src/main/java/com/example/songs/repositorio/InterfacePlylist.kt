package com.example.songs.repositorio

import androidx.media3.common.MediaItem

interface InterfacePlylist {
  suspend  fun criarPlyList(nome:String)
  suspend  fun criarPlaylist(nome: String,mediaItem: MediaItem)


}