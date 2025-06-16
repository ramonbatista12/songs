package com.songsSongs.songs.servicoDemidia.Equalizacao

import android.content.Context
import android.media.audiofx.Equalizer
import android.util.Log
import com.songsSongs.songs.servicoDemidia.AuxilarMediaSecion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class EqualizadorInterno(val equalizer:Equalizer,val contextoDoServico:Context ):InterfaceDeEqualizacao, AuxilarMediaSecion {
    private var job: Job? = Job()
    private val scope = CoroutineScope(Dispatchers.Default+job!!)
    val ganhos = MutableStateFlow<List<MutableStateFlow<Short>>>(emptyList())
    val numeroDeBandas=equalizer.numberOfBands
    private val presents=Presents(this,contextoDoServico, acaoUsePresent = {tipo->
        usePreset(tipo)
    }, acaoSetCustonPresent = {
        bandasDeAudio,ganhos->
        setCustonPresent(bandasDeAudio,ganhos)
    } )
    val presentsSelecionado= presents.present


    init {
        ganhos.value = List<MutableStateFlow<Short>>(size = equalizer.numberOfBands.toInt()) {MutableStateFlow(0)}
         scope.launch {
             coletaFluxoDeGanho()
         }
    }


    override fun equalizarBanda(banda: Int, dcbels: Short) {
        scope.launch {
            Log.i("equalizador", " no equalizar banda banda $banda dcbels $dcbels")
            equalizer.setBandLevel(banda.toShort(), (dcbels * 100).toShort())
        }

    }
     fun setCustonPresent(bandasDeAudio: List<Int>, ganhos: List<Short>) {
         for (banda in 0..bandasDeAudio.size-1) {
             Log.i("equalizador", "nosetCustom banda $banda ganho ${ganhos[banda]}")
             equalizer.setBandLevel(banda.toShort(),ganhos[banda])
         }
     }
    fun salvarValoresDeEqualizacao(){
        val mutableListCanais = mutableListOf<Int>()
        val mutableListGanhos = mutableListOf<Short>()

        for (i in 0..numeroDeBandas-1) {
            mutableListGanhos.add(equalizer.getBandLevel(i.toShort()))
            mutableListCanais.add(i)
        }
        presents.salvarValoresDeEqualizacao(mutableListCanais,mutableListGanhos)


    }
    fun setPresent(tipo:String){
        scope.launch {
            presents?.setPresent(tipo)
            if(tipo!=presents!!.custon&& presents!!.checarPresent(tipo)){
                usePreset(presents!!.presents[tipo]!!)

            }}
    }
    override fun ativar():Int =equalizer.setEnabled(true)
    override fun desativar(): Int=equalizer.setEnabled(false)
    override fun getIdDoEfeirto(): Int =equalizer.id
    override fun fluxoDeBandasDeAudio(): Flow<List<Int>> = flow<List<Int>> {
        while (true) {
            val lista = mutableListOf<Int>()
            for (i in 0..equalizer.numberOfBands - 1) {
                lista.add(equalizer.getCenterFreq(i.toShort()))
            }
            emit(lista)
            delay(3000)
        }
    }
    override fun fluxoDeGanhos(): Flow<List<Short>> =flow<List<Short>> {
        while (true) {
            val lista = mutableListOf<Short>()
            for (i in 0..equalizer.numberOfBands - 1) {
                lista.add(equalizer.getBandLevel(i.toShort()))
            }
            emit(lista)
            delay(1000)
        }
    }
    fun fluxoChavesDePresents()= presents.fluxoChavesDePresents()
    private suspend fun coletaFluxoDeGanho(){
        scope.launch {
            for (i in 0..equalizer.numberOfBands-1){
                scope.launch {
                    val indice =i
                    while (true){
                        ganhos.value[indice].emit(equalizer.getBandLevel(indice.toShort()))
                        delay(100)
                    }
                }

            }

        }
    }

    override fun finalizar() {
        if (job!=null) job?.cancel()
        job=null
    }

    override fun usePreset(tipo: Short) {
      scope.launch {
          equalizer.usePreset(tipo)
      }
    }
}