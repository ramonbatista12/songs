package com.example.songs.repositorio

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class ListaPlaylist(@PrimaryKey(autoGenerate = true)val id:Long,
                         @ColumnInfo(name = "nome")val nome:String)


@Entity(foreignKeys = [
    ForeignKey(entity = ListaPlaylist::class,
    parentColumns = ["id"],
    childColumns = ["idPlylist"],
    onDelete = ForeignKey.CASCADE,
    onUpdate = ForeignKey.NO_ACTION,
    deferred = false
)
],indices = [androidx.room.Index("idPlylist")])

data class ItemsDeMedia(@PrimaryKey(autoGenerate = true)val id:Long,
                        val idPlylist:Long,
                        val titulo:String,
                        val artista:String,
                        val album:String,
                        val uri:String,
                        val idMedia:String,
                        val duracao:Long )