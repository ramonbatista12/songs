package com.songsSongs.songs.repositorio

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ListaPlaylist::class,ItemsDeMedia::class), version = 1, exportSchema = true)
abstract class RoomBd: RoomDatabase() {
    abstract fun dao(): Daos
}