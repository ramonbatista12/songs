package com.example.songs.servicoDemidia

import android.media.audiofx.Equalizer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class Equalizador(prio:Int,val tipo:Int):AuxilarMediaSecion  {
      private val TAG = "Equalizador"
      val equalizer =Equalizer(prio,tipo)
      val  scope = CoroutineScope(Dispatchers.Main)
      val  frquenciasNasBandas=Array<MutableStateFlow<Int>>(equalizer.numberOfBands.toInt(), init = { MutableStateFlow<Int>(0) })
     init {
         scope.launch {
             equalizer.usePreset(2)

              Log.d(TAG,"numeros de bandas de audio disponiveis ${equalizer.numberOfBands} ,size estados ${frquenciasNasBandas.size}")
             for (i in 0 until equalizer.numberOfBands) {
                 scope.launch {
                     fluxoFequenciaPorBanda(i.toShort()).collect{
                         frquenciasNasBandas[i.toInt()].value=it
                     }
                 }
             }
          launch {
              while (true){
                  for (i in 0 until equalizer.numberOfBands){
                      Log.d(TAG,"banda ${i} , frequencia ${frquenciasNasBandas[i].value}")}
                  delay(3000)
              }
          }


                   }
     }
    fun fluxoFequenciaPorBanda(indice:Short)= flow<Int>{
        while (true){
            emit(equalizer.getBandLevel(indice.toShort()).toInt())
            delay(1000)


        }

    }
    suspend fun setGanho(banda:Short,ganho:Int){
        equalizer.setBandLevel(banda,ganho.toShort())
    }
    suspend fun setPreset(preset:Int){
        equalizer.usePreset(preset.toShort())
    }
    override fun finalizar() {
       equalizer.release()
    }

}

class Tipos{
    val opcoes= arrayOf("rock","acustic","jazz","defalt","custon")
    val map=HashMap<String,DoubleArray>()

    init {
        map.put(opcoes[0], doubleArrayOf())
        map.put(opcoes[1], doubleArrayOf())
        map.put(opcoes[2], doubleArrayOf())
        map.put(opcoes[3], doubleArrayOf())


    }

   fun graves(equalizer: Equalizer){


   }
   fun medios(){

   }
   fun agudos(){

   }

}

