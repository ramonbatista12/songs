package com.songsSongs.songs.servicoDemidia

import android.content.Context
import android.media.audiofx.Equalizer
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.songsSongs.songs.application.AplicationCuston.conteiner.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

class Equalizador(val prioridade:PrioridadesDaEqualizacao,val idDaSesaoDaMedia:Int,val comtextoDoServico: Context?=null):AuxilarMediaSecion {
    private val TAG = "Equalizador"
    val equalizer = Equalizer(prioridade.prioridade, idDaSesaoDaMedia)
    var job:Job = Job()
    val scope = CoroutineScope(Dispatchers.Default+job)
    val identificadorPresendet = MutableStateFlow(0)

    val sharedreferrence=comtextoDoServico?.getSharedPreferences("equalizador",Context.MODE_PRIVATE)
    val dataStore=comtextoDoServico?.dataStore
    val stringTipopresent= MutableStateFlow("")
    val graves= MutableStateFlow<Short>(0)
    val medios= MutableStateFlow<Short>(0)
    val agudos= MutableStateFlow<Short>(0)
    val arrayDeChaves= arrayOf<Preferences.Key<String>>(stringPreferencesKey("TipoDePreset"),
                                                        stringPreferencesKey("Graves"),
                                                        stringPreferencesKey("Medio"),
                                                        stringPreferencesKey("Agudos"))

    init {
     scope.launch(Dispatchers.IO) {
         scope.launch {
            dataStore?.data?.map {
             it[arrayDeChaves[0]] ?: ""
            }?.collect{
                stringTipopresent.value=it
            }
         }
         scope.launch {
             dataStore?.data?.map {
            it[arrayDeChaves[1]]?: "0"

             }?.collect{
                 graves.value=it.toShort()
             }
         }
         scope.launch {
             dataStore?.data?.map {
             it[arrayDeChaves[2]] ?: "0"

             }?.collect{
                 medios.value=it.toShort()
             }
         }
         scope.launch {
             dataStore?.data?.map {
              it[arrayDeChaves[3]] ?: "0"
             }?.collect{
                 agudos.value=it.toShort()
             }
         }


     }


    }

    fun equalizarBanda(banda:Int,dcbels:Short){
      scope.launch {
          val bandas= equalizer.numberOfBands
          val arraydeBAndas=equalizer.bandLevelRange
          for (i in 0 until arraydeBAndas.size-1){

          }
      }

    }

    fun equalizar(tipo:String,arrayDeGanhos:Array<Short>){
        scope.launch {
            Log.i(TAG,"equalizando numero de bandas de audio ${equalizer.numberOfBands}")
            for (i in 0 .. equalizer.numberOfBands-1){

                val frequenciaDaBanda = equalizer.getCenterFreq(i.toShort())
                if(frequenciaDaBanda<=230_000){
                    Log.i(TAG,"frequencia da banda grave $frequenciaDaBanda" )
                    equalizer.setBandLevel(i.toShort(),arrayDeGanhos[0])
                    Log.i(TAG,"frequencia da banda grave ${equalizer.getBandLevel(i.toShort())}" )
                }

                else if(frequenciaDaBanda>=230_000&&frequenciaDaBanda<=3_600_000){
                    Log.i(TAG,"frequencia da banda meias $frequenciaDaBanda" )
                    equalizer.setBandLevel(i.toShort(),arrayDeGanhos[1])
                    Log.i(TAG,"frequencia da banda medias ${equalizer.getBandLevel(i.toShort())}" )
                }

                else{
                    Log.i(TAG,"frequencia da banda aguda $frequenciaDaBanda" )
                    equalizer.setBandLevel(i.toShort(),arrayDeGanhos[2])
                    Log.i(TAG,"frequencia da banda aguda ${equalizer.getBandLevel(i.toShort())}" )
                }


            }

        }
    }

    fun fluxoPresents()= flow<Equalizacao> {
         when (stringTipopresent.value){
             ""->emit(Equalizacao("",graves.value,medios.value,agudos.value))
             "rock"->emit(Equalizacao("rock",3,-3,3))
             "pop"->emit(Equalizacao("pop",3,3,3))
             "classic"->emit(Equalizacao("classic",3,0,3))
             "country"->emit(Equalizacao("country",0,0,0))
             "custom"->emit(Equalizacao("custom",graves.value,medios.value,agudos.value))

         }
    }

    override fun finalizar() {
        job.cancel()

    }


}

sealed class PrioridadesDaEqualizacao(val prioridade:Int){
    object Baixa:PrioridadesDaEqualizacao(prioridade = -1)
    object Media:PrioridadesDaEqualizacao(prioridade = 0)
    object Alta:PrioridadesDaEqualizacao(prioridade = 1)}

data class Equalizacao(val presnt:String,val graves:Short,val medios:Short,val agudos:Short)

sealed class opcoesDeEqualizacao(val opcao:String){
    object Rock:opcoesDeEqualizacao(opcao = "rock")
    object Pop:opcoesDeEqualizacao(opcao = "pop")
    object Classic:opcoesDeEqualizacao(opcao = "classic")
    object Country:opcoesDeEqualizacao(opcao = "country")
    object custom:opcoesDeEqualizacao(opcao = "custom")

}

class Presents{

    val map:Map<String,ShortArray>? = mapOf<String,ShortArray>(
        "rock" to shortArrayOf(3,-3,3),
        "pop" to shortArrayOf(3,3,3),
        "classic" to shortArrayOf(3,0,3),
        "country" to shortArrayOf(0,0,0),
    )

}



