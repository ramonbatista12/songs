package com.songsSongs.songs.servicoDemidia

import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.session.MediaSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*
* classe HelperPalyerEstados
* classe responsavel por gerenciar os estados do player
* como  loding reproducao
* os estados sao emitidos por flow e coletados por collect para gerenciar os
* estados do player que podem ser pasados para a tela principal ou para o view model
*
* */


class HelperPalyerEstados(val mediaSession: MediaSession): AuxilarMediaSecion {
    private val job = Job()
    private val scopoCorotina = CoroutineScope(Dispatchers.Main + job)
    private val tempoDereproducao = MutableStateFlow(0L)
    private val tempoTotal = MutableStateFlow(0L)
    private val estaReproduzindo = MutableStateFlow(false)//
    private val loding = MutableStateFlow(false)
    private val caregando = MutableStateFlow(false)
    private val metadataAtual= MutableStateFlow<MediaItem?>(null)
    private val modoAleatorio = MutableStateFlow(false)
    private val modoRepeticao = MutableStateFlow(0)
    private val emplyer = MutableStateFlow(false)
    private val indice= MutableStateFlow(0)
    var _tempoTotal = tempoTotal.stateIn(scopoCorotina, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), 0L)
    var _tempoDereproducao = tempoDereproducao.stateIn(scopoCorotina, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), 0L)
    var _estaReproduzindo = estaReproduzindo.stateIn(scopoCorotina, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), false)
    val caregando_ = caregando.stateIn(scopoCorotina, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), false)
    var _metadataAtual = metadataAtual.stateIn(scopoCorotina, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), null)
    var _modoAleatorio = modoAleatorio.stateIn(scopoCorotina, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), false)
    val _modoRepeticao = modoRepeticao.stateIn(scopoCorotina, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), 0)
    val _emplyer = emplyer.stateIn(scopoCorotina, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), false)
    val _indice=indice
    init {
        scopoCorotina.launch {
            fluxoTempoDereproducao().collect {
                tempoDereproducao.value = it

            }
        }
        scopoCorotina.launch {
            fluxoTempoTotal().collect {
                tempoTotal.value = it
            }
        }
        scopoCorotina.launch {
            fluxoEmplyer().collect {
                estaReproduzindo.value = it
            }
        }
        scopoCorotina.launch {
            fluxoLoding().collect {
                loding.value = it
            }
        }
        scopoCorotina.launch {
            metaDataAtual().collect{
                metadataAtual.value=it
            }

        }
        scopoCorotina.launch {
            fluxoModoAleatorio().collect{
                modoAleatorio.value=it
            }
        }
        scopoCorotina.launch {
            fluxoModoRepeticao().collect{
                modoRepeticao.value=it
            }
        }

        scopoCorotina.launch {
            fluxoCaregando().collect{
                caregando.value=it
            }
        }
        scopoCorotina.launch {
            indixeAtual().collect {
            indice.value=it

            }
        }

    }





private fun fluxoTempoDereproducao()= flow<Long> {

     while (true) {

         emit(mediaSession.player.currentPosition)
         delay(1000)
     }
 }

 private fun fluxoTempoTotal()= flow<Long> {
     while (true){

         emit(mediaSession.player.contentDuration)
         delay(1000)
     }
 }

 private fun fluxoEmplyer()= flow<Boolean> {
     while (true){

         emit(mediaSession.player.isPlaying)
         delay(1000)
     }
 }

private fun fluxoLoding()= flow<Boolean> {
    while (true){
        emit(mediaSession.player.isLoading)
        delay(1000)
    }
}

private fun metaDataAtual()= flow<MediaItem?> {
  while (true){
   emit(mediaSession.player.currentMediaItem)
    delay(1000)
  }
}

private fun fluxoModoAleatorio()= flow<Boolean> {
   while (true) {
    emit(mediaSession.player.shuffleModeEnabled)
    delay(1000)}
}
private fun fluxoModoRepeticao()= flow<Int> {
    while (true){
     emit(mediaSession.player.repeatMode)
    delay(1000)}

}

private fun fluxoCaregando()= flow<Boolean> {
    while (true){
        emit(mediaSession.player.isLoading)
        delay(1000)
    }
}

private fun indixeAtual()= flow<Int> {
    while (true){
        emit(mediaSession.player.currentMediaItemIndex)
        delay(1000)
    }
    }



   override fun finalizar(){
        job.cancel()

    }

}


class HelperPalyerComandes(val mediaSession: MediaSession): ComandosDemedia,AuxilarMediaSecion{
    private val job= Job()
    private val scopoCorotina= CoroutineScope(Dispatchers.Main+job)
    override fun prepare() {
        scopoCorotina.launch {

            mediaSession.player.prepare()
        }
    }

    override fun play() {

        scopoCorotina.launch {mediaSession.player.play()}
    }

    override fun pause() {
        scopoCorotina.launch {
            mediaSession.player.pause()
        }
    }

    override fun stop() {
        mediaSession.player.stop()
    }

    override fun seekTo(position: Long) {
       scopoCorotina.launch {
           mediaSession.player.seekTo(position)
       }
    }

    override fun seekToItem(position: Int) {
       scopoCorotina.launch {
           mediaSession.player.seekTo(position,0)
       }
    }

    override fun setLista(lista: List<MediaItem>) {
       scopoCorotina.launch {
           mediaSession.player.clearMediaItems()
           mediaSession.player.setMediaItems(lista)
       }
    }

    override fun setMediaItem(mediaItem: MediaItem) {
       scopoCorotina.launch {
           mediaSession.player.setMediaItem(mediaItem)
       }
    }

    override fun setModoRepeticao(modo: Int) {
        scopoCorotina.launch {
            Log.i("setmod","set mod helpercomandes modo $modo")
            mediaSession.player.repeatMode = modo
        }
    }

    override fun setModoAleatorio(shuffleModeEnabled: Boolean) {
        scopoCorotina.launch {
            mediaSession.player.shuffleModeEnabled=shuffleModeEnabled
        }
    }

    override fun next() {
        scopoCorotina.launch {
            mediaSession.player.seekToNext()
        }

    }

    override fun preview() {
        scopoCorotina.launch {
            mediaSession.player.seekToPrevious()
        }

    }

    override fun finalizar() {
        job.cancel()
    }

}




