package com.songsSongs.songs.servicoDemidia.Equalizacao

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class Presents(val ponteiroParaEqualizador: EqualizadorInterno,
               val contexto: Context,
               val acaoUsePresent:(short:Short)->Unit,
               val acaoSetCustonPresent:(bandasDeAudio:List<Int>,ganhos:List<Short>)->Unit){
    val presents= mutableMapOf<String,Short>()
    val chavePresents= mutableListOf<String>()
    val custon="custon"
    var present= MutableStateFlow<String>("")
    val dataStore= DataStore(context = contexto,
        acaoDeAtualizarOEqualizador = {tipo,bandasDeAudio,ganhos->
                                      setPresent(tipo)
                                     if(tipo==custon){
                                        acaoSetCustonPresent(bandasDeAudio,ganhos)
                                        return@DataStore }
                                     if(checarPresent(tipo))
                                         acaoUsePresent(presents[tipo]!!)})

    init {
        chavePresents.add(custon)
        val numeroDePresents =ponteiroParaEqualizador.equalizer.numberOfPresets
        for (i in 1..numeroDePresents-1){
            val s =ponteiroParaEqualizador.equalizer.getPresetName(i.toShort())
            presents.put(s,i.toShort())
            chavePresents.add(s)

        }
        presents.put("custon",-1)
    }

     fun setPresent(presents:String) {present.value=presents}

    fun salvarValoresDeEqualizacao(listaDeBandasDeAudio: List<Int>,listaDeGanhos: List<Short>){
        if (present.value == custon) {
            dataStore?.salvarValoresDeEqualizacao(present.value,listaDeBandasDeAudio,listaDeGanhos)
            return
        }
        dataStore ?.salvarValoresDeEqualizacao(tipo = present.value, listOf(), listOf())
    }



    fun fluxoDePresents()= flow<String>{
        while (true){
            emit(present.value)
            delay(3000)
        }
    }

    fun fluxoChavesDePresents()= flow<List<String>>{
        while (true) {
            if (presents == null) emit(emptyList())
            else emit(chavePresents)
            delay(3000)

        }
    }

    fun checarPresent(tipo:String):Boolean{
        return presents.containsKey(tipo)
    }

    fun finalizar(){
        dataStore.finalizar()
    }

}
