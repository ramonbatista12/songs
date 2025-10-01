package com.songsSongs.songs.viewModels

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.songsSongs.songs.repositorio.RepositorioService
import com.songsSongs.songs.servicoDemidia.ComandosDemedia
import com.songsSongs.songs.servicoDemidia.HelperPalyerEstados
import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class VmodelPlayer(val estadoService: MutableStateFlow<ResultadosConecaoServiceMedia>,val r:RepositorioService):ViewModel(),
    ComandosDemedia {
  private val duracao=MutableStateFlow(0f)
  private val duracaoString=MutableStateFlow("00:00:00")
  private val tempoTotal=MutableStateFlow(0L)
  private val emreproducao=MutableStateFlow(false)
  private val caregando=MutableStateFlow(false)
  private val mediaItemAtual=MutableStateFlow<MediaItem?>(null)
  private val modoAleatorio=MutableStateFlow(false)
  private val tempoTotalString=MutableStateFlow("00:00:00")
  private val modoRepeticao=MutableStateFlow<ModoDerepeticao>(ModoDerepeticao.Desativado)
  private val indice=MutableStateFlow(0)
  private val imagemPlyer=MutableStateFlow<ImagemPlyer>(ImagemPlyer.Vazia())
  private val scope= viewModelScope
  val _duracao=duracao.asStateFlow()
  val _emreproducao=emreproducao.asStateFlow()
  val _caregando=caregando.asStateFlow()
  val _tempoTotal=tempoTotal.asStateFlow()
  val _mediaItemAtual=mediaItemAtual.asStateFlow()
  val _modoAleatorio=modoAleatorio.asStateFlow()
  val _modoRepeticao=modoRepeticao.asStateFlow()
  val _tempoTotalString=tempoTotalString.asStateFlow()
  val _duracaoString=duracaoString.asStateFlow()
  val _indice=indice.asStateFlow()
  val _imagemPlyer=imagemPlyer.asStateFlow()
  private var job:Job?=null


init {
     scope.launch(Dispatchers.Default) {
       estadoService.collect{
               when(it){
                 is ResultadosConecaoServiceMedia.Conectado->{
                     val ponteiro=it.setvice.helperPalyer
                     coleta(ponteiro = ponteiro!!)

                 }
                 is ResultadosConecaoServiceMedia.Desconectado->{
                    if(job!=null) job!!.cancel()
                 }
                 is ResultadosConecaoServiceMedia.Erro->{
                    if(job!=null) job!!.cancel()
                 }
             }


         }
     }
}


   suspend private fun coleta(ponteiro:HelperPalyerEstados){
       if(job!=null) job!!.cancel()
       job=scope.launch(Dispatchers.Default) {

       scope.launch(Dispatchers.Default)  {    ponteiro._tempoTotal.collect{
             tempoTotal.value=it
         } }
       scope.launch(Dispatchers.Default)  {
               ponteiro._tempoDereproducao.map {
                  // Log.i("coleta","it $it,tempoTotal ${tempoTotal.value}")
                    (it*100f) /tempoTotal.value
               }.collect{
                   duracao.value=it
               }
          }
       scope.launch(Dispatchers.Default) {
           ponteiro._tempoDereproducao.map {
               val horas=(it/3600000).toInt()
               val minutos=((it-horas*3600000)/60000).toInt()
               val segundos=((it-horas*3600000-minutos*60000)/1000).toInt()
               val  s="${if(horas<10) "0$horas" else horas}:${if(minutos<10) "0$minutos" else minutos}:${if(segundos<10) "0$segundos" else segundos}"
               s
           }.collect{

               duracaoString.value=it
           }
       }
       scope.launch {
               val combine= combine(ponteiro._estaReproduzindo,ponteiro.caregando_){caregando,reproduzindo->
                    when{
                        caregando&&!reproduzindo->{
                            PlayerResultado.Caregando
                        }
                        !caregando&&reproduzindo->{
                            PlayerResultado.Reproduzindo
                        }
                        else->{
                            PlayerResultado.Parado
                        }
                    }
               }.collect{

               }
           }
       scope.launch(Dispatchers.Default) {

               ponteiro._metadataAtual.collect{

                   mediaItemAtual.emit(it)
               }
           }
       scope.launch(Dispatchers.Default) {
               ponteiro._estaReproduzindo.collect{
                   emreproducao.value=it
               }
           }
       scope.launch(Dispatchers.Default) {
           ponteiro._modoAleatorio.collect{
               modoAleatorio.value=it
           }
       }
       scope.launch(Dispatchers.Default) {
           ponteiro._tempoTotal.map {
               val horas=(it/3600000).toInt()
               val minutos=((it-horas*3600000)/60000).toInt()
               val segundos=((it-horas*3600000-minutos*60000)/1000).toInt()
               val  s="${if(horas<10) "0$horas" else horas}:${if(minutos<10) "0$minutos" else minutos}:${if(segundos<10) "0$segundos" else segundos}"
               s
           }.collect{
               tempoTotalString.value=it
           }
       }
       scope.launch(Dispatchers.Default) {
           ponteiro._modoRepeticao.map {
               when(it){
                   0->ModoDerepeticao.Desativado
                   1->ModoDerepeticao.RepetirEssa
                   2->ModoDerepeticao.RepetirTodos
                   else->ModoDerepeticao.Desativado
               }
           }.collect{
               modoRepeticao.value=it
           }
       }
       scope.launch {
          ponteiro.caregando_.collect{
              caregando.value=it
          }
      }
       scope.launch {
          ponteiro._indice.collect{
              indice.value=it
          }
      }

       }


   }

   override fun onCleared() {
     if(job!=null) job!!.cancel()
      super.onCleared()
  }

   override fun prepare() {
        scope.launch {
            when(val r =estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                       r.setvice.helperPalyerComandes?.prepare()

                    }catch (e:Exception){

                    }
                }
                else->{}
            }

        }
    }

   override fun play() {
        scope.launch {
            when(val r=estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                        r.setvice.helperPalyerComandes?.play()

                    }catch (e:Exception){

                    }
                }
                else->{}
            }
        }

    }

   override fun pause() {
        scope.launch {
            when(val r=estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                        r.setvice.helperPalyerComandes?.pause()
                    }catch (e:Exception){

                    }
                }
                else->{}
            }
        }

    }

   override fun stop() {
        scope.launch {
            when(val r=estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                        r.setvice.helperPalyerComandes?.stop()
                    }catch (e:Exception){

                    }
                }
                else->{}
            }
        }

    }

   override fun seekTo(position: Long) {
        scope.launch {
            when(val r=estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                        r.setvice.helperPalyerComandes?.seekTo(position)
                    }catch (e:Exception){

                    }
                }
                else->{}
            }
        }

    }

   override fun seekToItem(position: Int) {
        scope.launch {
            when(val r=estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                        r.setvice.helperPalyerComandes?.seekToItem(position)
                    }catch (e:Exception){}}
                else->{}
            }
        }
    }

   override fun setLista(lista: List<MediaItem>) {
        scope.launch {when(val r=estadoService.value){
            is ResultadosConecaoServiceMedia.Conectado->{
                try {
                    r.setvice.helperPalyerComandes?.setLista(lista)
                }catch (e:Exception){

                }
            }
            else->{}
        }  }

    }

   override fun setMediaItem(mediaItem: MediaItem) {
        scope.launch {
        when(val r=estadoService.value){
            is ResultadosConecaoServiceMedia.Conectado->{
                try {
                  r.setvice.helperPalyerComandes?.setMediaItem(mediaItem)
                }catch (e:Exception){

                }
            }
            else->{}
        }}
    }

   override fun setModoRepeticao(modo: Int) {

        scope.launch {
            when(val r=estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                        r.setvice.helperPalyerComandes?.setModoRepeticao(modo)
                    }catch (e:Exception){

                    }}
                else->{}
            }
        }


    }

   fun setModoDeRepeticao(modo:ModoDerepeticao){
       Log.i("setmod","entrou modo de repeticao pro modelo $modo")
       when(val m=modo){
           is ModoDerepeticao.Desativado->setModoRepeticao(m.valor)
           is ModoDerepeticao.RepetirEssa->setModoRepeticao(m.valor)
           is ModoDerepeticao.RepetirTodos->setModoRepeticao(m.valor)
       }
   }

   override fun setModoAleatorio(shuffleModeEnabled: Boolean) {
      scope.launch {
          when(val r =estadoService.value) {
              is ResultadosConecaoServiceMedia.Conectado -> {
                  try {
                      r.setvice.helperPalyerComandes?.setModoAleatorio(shuffleModeEnabled)
                  } catch (e: Exception) {
                  }
              }

              else -> {}
          }
      }
    }

   override fun next() {
        scope.launch {
            when(val r= estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                        r.setvice.helperPalyerComandes?.next()
                    }catch (e:Exception){

                    }
                }
                else->{}
            }
        }

    }

   override fun preview() {
        scope.launch {
            when(val r=estadoService.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    try {
                        r.setvice.helperPalyerComandes?.preview()
                    }catch (e:Exception){

                    }
                }
                else->{}
            }
        }

    }

   fun carregarLista(lista:List<MediaItem>,id:Int){
        setLista(lista)

        seekToItem(id)
        prepare()

    }

    suspend fun obterImagem(uri: Uri,id:Long):Deferred< Bitmap?>{
      return scope.async (Dispatchers.IO) {
           val bitimap = r.getMetaData(uri, id)
          return@async bitimap
         }}
    suspend fun obterImagem(uri: Uri,id:Long,whith:Int=600,heigth:Int=600):Deferred< Bitmap?>{
        return scope.async (Dispatchers.IO) {
            val bitimap = r.getMetaData(uri,0)
            return@async bitimap
        }}
    suspend fun acaoMudarImagemCOmpartilhada(bitmap: Bitmap?){

            if (bitmap==null){
                imagemPlyer.emit(ImagemPlyer.Vazia())
                return
             }

            imagemPlyer.emit(ImagemPlyer.Imagem(bitmap!!))

    }

    suspend fun caregarImagemCOmpartilhada(uri: Uri){
        val bitmap=obterImagem(uri,0).await()
        acaoMudarImagemCOmpartilhada(bitmap)
    }



}


class FabricaViewmodelPlyer(){
   fun fabricar( estadoService: MutableStateFlow<ResultadosConecaoServiceMedia>,r:RepositorioService)=

    object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VmodelPlayer(estadoService=estadoService,r) as T
        }
    }
}