package com.songsSongs.songs.servicoDemidia.Equalizacao

import kotlinx.coroutines.flow.Flow

interface InterfaceDeEqualizacao {
    fun equalizarBanda(banda: Int, dcbels: Short)
    fun ativar(): Int
    fun desativar(): Int
    fun getIdDoEfeirto(): Int
    fun fluxoDeBandasDeAudio(): Flow<List<Int>>
    fun fluxoDeGanhos(): Flow<List<Short>>
    fun finalizar()
    fun usePreset(tipo:Short)



}