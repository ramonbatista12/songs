package com.songsSongs.songs.servicoDemidia.Equalizacao

import android.content.Context
import android.media.audiofx.Equalizer
import android.util.Log
import com.songsSongs.songs.servicoDemidia.AuxilarMediaSecion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Equalizador(
    val prioridade: PrioridadesDaEqualizacao,
    val idDaSesaoDaMedia: Int,
    val comtextoDoServico: Context? = null
) : AuxilarMediaSecion,InterfaceDeEqualizacao {
    private val TAG = "Equalizador"
    val equalizer = Equalizer(prioridade.prioridade, idDaSesaoDaMedia)
    private var job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val sharedreferrence =comtextoDoServico?.getSharedPreferences("equalizador", Context.MODE_PRIVATE)
    private val equalizadorInterno:EqualizadorInterno=EqualizadorInterno(equalizer,comtextoDoServico!!)
    val bandasDeAudio = MutableStateFlow<List<Int>>(emptyList())
    val listaDePresents = MutableStateFlow<List<String>>(emptyList())
    val presentSelecionado =equalizadorInterno.presentsSelecionado
    val ganhos=equalizadorInterno?.ganhos



    init {
     scope.launch {
            coleta()
        }
    }
    //funcoes de coleta de fluxo
    private suspend fun coleta(){
        scope.launch(Dispatchers.IO) {
           coletarBandasDeAudio()
           coletarFluxoDePresents()
      }
    }

    private suspend fun coletarBandasDeAudio() {
        scope.launch {
            fluxoDeBandasDeAudio().collect {
                bandasDeAudio.value = it
            }
        }
    }

    private suspend fun coletarFluxoDePresents(){
        scope.launch {
            fluxoChavesDePresents().collect {
                listaDePresents.value = it
            }
        }
    }

    private fun setCustonPresent(bandasDeAudio: List<Int>, ganhos: List<Short>) =equalizadorInterno.setCustonPresent(bandasDeAudio,ganhos)
    override fun getIdDoEfeirto(): Int =equalizadorInterno.getIdDoEfeirto()
    override fun usePreset(tipo: Short)=equalizadorInterno.usePreset(tipo)
    override fun ativar():Int =equalizadorInterno.ativar()
    override fun desativar():Int =equalizadorInterno.desativar()
    override fun equalizarBanda(banda: Int, dcbels: Short) =equalizadorInterno.equalizarBanda(banda, dcbels)
    override fun fluxoDeBandasDeAudio() = equalizadorInterno.fluxoDeBandasDeAudio()
    override fun fluxoDeGanhos() = equalizadorInterno.fluxoDeGanhos()
    fun salvarValoresDeEqualizacao() =equalizadorInterno.salvarValoresDeEqualizacao()
    suspend fun setPresent(tipo:String)= equalizadorInterno.setPresent(tipo)
    private fun fluxoChavesDePresents() = equalizadorInterno.fluxoChavesDePresents()

   override fun finalizar() {
        equalizadorInterno.finalizar()
        job.cancel()
        equalizer.release()


    }


}

sealed class PrioridadesDaEqualizacao(val prioridade:Int){
    object Baixa: PrioridadesDaEqualizacao(prioridade = -1)
    object Media: PrioridadesDaEqualizacao(prioridade = 0)
    object Alta: PrioridadesDaEqualizacao(prioridade = Int.MAX_VALUE)}





@Serializable
class ValoresDeEqualizacao(val tipo:String,val bandsaDeAudio: List<Int>,val ganhos:List<Short>)







