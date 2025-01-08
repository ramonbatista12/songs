package com.example.songs.viewModels

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.songs.application.AplicationCuston
import com.example.songs.servicoDemidia.HelperPalyerEstados
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import com.example.songs.servicoDemidia.ComandosDePlayer
import kotlinx.coroutines.flow.collect

@RequiresApi(Build.VERSION_CODES.Q)
class VmodelPlayer(val estadoService: MutableStateFlow<ResultadosConecaoServiceMedia>) :
    ViewModel(),
    ComandosDePlayer {
    private val duracao = MutableStateFlow(0f)
    private val tempoTotal = MutableStateFlow(0L)
    private val emreproducao = MutableStateFlow(false)
    private val caregando = MutableStateFlow(false)
    private val metadataAtual = MutableStateFlow<MediaItem?>(null)
    private val repeticao = MutableStateFlow(0)
    private val aleatorio = MutableStateFlow(false)
    private val scope = viewModelScope
    val _duracao = duracao.asStateFlow()
    val _emreproducao = emreproducao.asStateFlow()
    val _caregando = caregando.asStateFlow()
    val _tempoTotal = tempoTotal.asStateFlow()
    val _metadataAtual = metadataAtual.asStateFlow()
    val _repeticao = repeticao.asStateFlow().map {
        when (it) {
            0 -> PlyerRepeticao.NaoRepetir
            1 -> PlyerRepeticao.RepetirUm
            2 -> PlyerRepeticao.RepetirTodos
            else -> PlyerRepeticao.NaoRepetir
        }
    }
    val _aleatorio = aleatorio.asStateFlow()


    private var job: Job? = null


    init {
        scope.launch {
            estadoService.collect {
                when (it) {
                    is ResultadosConecaoServiceMedia.Conectado -> {
                        val ponteiro = it.setvice.helperPalyer
                        coleta(ponteiro = ponteiro!!)

                    }

                    is ResultadosConecaoServiceMedia.Desconectado -> {
                        if (job != null) job!!.cancel()
                    }

                    is ResultadosConecaoServiceMedia.Erro -> {
                        if (job != null) job!!.cancel()
                    }
                }


            }
        }
    }


    suspend private fun coleta(ponteiro: HelperPalyerEstados) {
        if (job != null) job!!.cancel()
        job = scope.launch {

            scope.launch {
                ponteiro._tempoTotal.collect {
                    tempoTotal.value = it
                }
            }

            scope.launch {
                ponteiro._tempoDereproducao.map {
                    if (it == 0L) 0f
                    else
                        (it * 100f) / tempoTotal.value
                }.collect {
                    Log.d("duracao", "$it")
                    duracao.value = it
                }
            }

            scope.launch {
                ponteiro!!._estaReproduzindo.collect {
                    emreproducao.value = it
                }
            }
            scope.launch {
                ponteiro!!._metadataAtual.collect {
                    metadataAtual.value = it
                }
            }
            scope.launch {
                ponteiro!!._aleatorio.collect {
                    aleatorio.value = it
                }
            }
            scope.launch {
                ponteiro!!._repeticao.collect {
                    repeticao.value = it
                }
            }


        }


    }

    override fun onCleared() {
        if (job != null) job!!.cancel()
        super.onCleared()
    }

    override fun play() {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    Log.d("controler plyer", "ply chamado")
                    try {
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.play()
                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }
                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun pause() {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    try {
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.pause()
                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }


                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun stop() {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    try {
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.stop()
                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }

                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun seekTo(position: Long) {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    try {
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.seekTo(
                            position
                        )
                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }

                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun seekToIndisse(index: Int) {
        scope.launch {
            try {
                (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.seekToIndisse(
                    index
                )
            } catch (e: Exception) {
        }
    }
    }

    fun seekTo(position: Float) {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    try {
                        val position = (position * tempoTotal.value).toLong()
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.seekTo(
                            position / 100
                        )

                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }

                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun setLista(lista: List<MediaItem>) {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    try {
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.setLista(
                            lista
                        )
                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }

                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun setMediaItem(mediaItem: MediaItem) {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    try {
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.setMediaItem(
                            mediaItem
                        )
                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }

                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun next() {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    try {
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.next()
                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }

                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun preview() {
        when (estadoService.value) {
            is ResultadosConecaoServiceMedia.Conectado -> {
                scope.launch {
                    try {
                        (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.preview()
                    } catch (e: Exception) {
                        Log.e("controler plyer", "erro ao conectar")
                    }

                }
            }

            else -> {
                Toast.makeText(AplicationCuston.context, "erro ao conectar", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun repeticao(repeticao: Int) {
        scope.launch {
            try {
                (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.repeticao(
                    repeticao
                )
            } catch (e: Exception) {

            }
        }
    }

    fun repeticao(repeticao: PlyerRepeticao) {
        when (val r =repeticao) {
            is PlyerRepeticao.NaoRepetir -> repeticao(r.repeticao)
            is PlyerRepeticao.RepetirUm -> repeticao(r.repeticao)
            is PlyerRepeticao.RepetirTodos -> repeticao(r.repeticao)

        }

    }

    override fun repeticao() {

    }

    override fun aleatorio(aleatorio: Boolean) {
        scope.launch {
            try {
                (estadoService.value as ResultadosConecaoServiceMedia.Conectado).setvice.helperPalyerComandes?.aleatorio(
                    aleatorio
                )
            } catch (e: Exception) {
            }
        }


    }
}

class FabricaViewmodelPlyer(){
   fun fabricar( estadoService: MutableStateFlow<ResultadosConecaoServiceMedia>)=

    object : ViewModelProvider.Factory{
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VmodelPlayer(estadoService=estadoService) as T
        }
    }
}