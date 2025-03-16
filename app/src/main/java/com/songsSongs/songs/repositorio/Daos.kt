package com.songsSongs.songs.repositorio

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Daos {
    @Query("SELECT * FROM ListaPlaylist")
    fun fluxoPlyList(): Flow<List<ListaPlaylist>>
    @Query("SELECT * FROM ItemsDeMedia WHERE idPlylist = :id")
    fun fluxoDeItemsDeMedia(id: Long): Flow<List<ItemsDeMedia>>
    @Query("SELECT idPlylist,uri,idMedia FROM ItemsDeMedia WHERE idPlylist = :id ORDER BY idMedia DESC LIMIT 4 ")
    suspend fun tumbmails(id: Long): List<ImagemTumbmail>
    @Query("SELECT * FROM ListaPlaylist ORDER BY nome DESC  limit  1 offset 1")
    suspend fun plyListCriada(): ListaPlaylist?
    @Query("DELETE FROM itemsdemedia WHERE idMedia = :idMedia")
    suspend fun removerItemDeMedia(idMedia:String)

    @Insert
    suspend fun inserirPlylist(plylist: ListaPlaylist)
    @Insert
    suspend fun inserirItemDeMedia(itemsDeMedia: ItemsDeMedia)
    @Delete
    suspend fun deletarPlylist(plylist: ListaPlaylist)
    @Delete
    suspend fun deletarItemDeMedia(itemsDeMedia: ItemsDeMedia)
    @Update
    suspend fun atualizarPlylist(plylist: ListaPlaylist)
    @Update
    suspend fun atualizarItemDeMedia(itemsDeMedia: ItemsDeMedia)


}
data class ImagemTumbmail(@ColumnInfo(name = "uri") val uri:String,@ColumnInfo(name = "idMedia")val idMedia:String)