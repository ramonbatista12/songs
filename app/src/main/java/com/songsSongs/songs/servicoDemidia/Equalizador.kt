package com.songsSongs.songs.servicoDemidia

import android.content.Context
import android.media.audiofx.Equalizer
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.songsSongs.songs.application.AplicationCuston.conteiner.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class Equalizador(
    val prioridade: PrioridadesDaEqualizacao,
    val idDaSesaoDaMedia: Int,
    val comtextoDoServico: Context? = null
) : AuxilarMediaSecion {
    private val TAG = "Equalizador"
    val equalizer = Equalizer(prioridade.prioridade, idDaSesaoDaMedia)
    private var job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val sharedreferrence =comtextoDoServico?.getSharedPreferences("equalizador", Context.MODE_PRIVATE)
    private var presents: Presents? = Presents(this)
    private var dataStore:DataStore?=DataStore(comtextoDoServico!!,{tipo,bandasDeAudio,ganhos->
        if(presents!=null){
           scope.launch {
               Log.i("presents","tipo do present ao coletar present ${tipo}")
               presents?.setPresent(tipo)
           }
            if(tipo==presents!!.chavePresents[0]){
                setCustonPresent(bandasDeAudio,ganhos)
            }else{
                if(presents!!.checarPresent(tipo))
                   equalizer.usePreset(presents!!.presents[tipo]!!)
            }
        }
    })
    val bandasDeAudio = MutableStateFlow<List<Int>>(emptyList())
    val listaDePresents = MutableStateFlow<List<String>>(emptyList())
    val presentSelecionado =MutableStateFlow<String>("")
    val ganhos = MutableStateFlow<List<MutableStateFlow<Short>>>(emptyList())


    init {
        ganhos.value = List<MutableStateFlow<Short>>(size = equalizer.numberOfBands.toInt()) {MutableStateFlow(0)}
        scope.launch {
            coleta()
        }
    }
    private suspend fun coleta(){
        scope.launch(Dispatchers.IO) {
           coletarBandasDeAudio()
           coletaFluxoDeGanho()
           coletarPresentsSelecionado()
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
            fluxoPresents().collect {
                listaDePresents.value = it
            }
        }
    }

    private suspend fun coletarPresentsSelecionado() {

        scope.launch {
            presents?.fluxoDePresents()?.collect{
                presentSelecionado.value=it
            }
        }
    }

    private suspend fun coletarPresentSelecionado(){
        scope.launch {
            presents?.fluxoDePresents()?.collect{
                presentSelecionado.value=it
            }
        }
    }

     suspend fun setPresent(tipo:String){
     scope.launch {
         Log.i("presents","tipo do present no equalizador ${tipo}")
         presents?.setPresent(tipo)
         if(tipo!=presents!!.chavePresents[0]&& presents!!.checarPresent(tipo)){
             Log.i("presents","no equalizador  presents afirma ter o tipo no hash map${tipo}")
              equalizer.usePreset(presents!!.presents[tipo]!!)
              presents?.setPresent(tipo)
         }



     }
    }



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

    private fun setCustonPresent(bandasDeAudio: List<Int>, ganhos: List<Short>) {
        for (banda in 0..bandasDeAudio.size-1) {
            equalizer.setBandLevel(banda.toShort(), ganhos[banda])
        }

    }

    fun idDoEfeirto(): Int =equalizer.id

    fun ativar() {
        val retorno = equalizer.setEnabled(true)}

    fun desativar() {equalizer.enabled = false}

    fun equalizarBanda(banda: Int, dcbels: Short) {
        scope.launch {
            equalizer.setBandLevel(banda.toShort(), (dcbels * 100).toShort())
        }

    }

    fun salvarValoresDeEqualizacao() {
        if (presentSelecionado.value == presents!!.chavePresents[0]) {
            val mutableListCanais = mutableListOf<Int>()
            val mutableListGanhos = mutableListOf<Short>()

            for (i in 0..equalizer.numberOfBands - 1) {
                mutableListGanhos.add(equalizer.getBandLevel(i.toShort()))
                mutableListCanais.add(i)
            }

            dataStore?.salvarValoresDeEqualizacao(presentSelecionado.value,mutableListCanais,mutableListGanhos)

        } else dataStore ?.salvarValoresDeEqualizacao(tipo = presentSelecionado.value, listOf(), listOf())

        }



    private fun fluxoPresents() = flow<List<String>> {
        while (true) {
            if (presents == null) emit(emptyList())
            else emit(presents!!.chavePresents)
            delay(3000)

        }
    }


    private fun fluxoDeBandasDeAudio() = flow<List<Int>> {
        while (true) {
            val lista = mutableListOf<Int>()
            for (i in 0..equalizer.numberOfBands - 1) {
                lista.add(equalizer.getCenterFreq(i.toShort()))
            }
            emit(lista)
            delay(3000)
        }
    }

    private fun fluxoDeGanhos() = flow<List<Short>> {
        while (true) {
            val lista = mutableListOf<Short>()
            for (i in 0..equalizer.numberOfBands - 1) {
                lista.add(equalizer.getBandLevel(i.toShort()))
            }
            delay(1000)
        }
    }

    override fun finalizar() {
        if (dataStore!=null) dataStore!!.finalizar()
        dataStore=null
        presents = null
        job.cancel()
        equalizer.release()


    }


}

sealed class PrioridadesDaEqualizacao(val prioridade:Int){
    object Baixa:PrioridadesDaEqualizacao(prioridade = -1)
    object Media:PrioridadesDaEqualizacao(prioridade = 0)
    object Alta:PrioridadesDaEqualizacao(prioridade = Int.MAX_VALUE)}


class Presents(val ponteiroParaEqualizador: Equalizador){
     val presents= mutableMapOf<String,Short>()
     val chavePresents= mutableListOf<String>()
     var present=MutableStateFlow<String>("")

    init {
        chavePresents.add("custon")
        val numeroDePresents =ponteiroParaEqualizador.equalizer.numberOfPresets
       for (i in 1..numeroDePresents-1){
           val s =ponteiroParaEqualizador.equalizer.getPresetName(i.toShort())
           presents.put(s,i.toShort())
           chavePresents.add(s)

       }
        presents.put("custom",-1)
    }

    suspend fun setPresent(presents:String) {
        Log.i("presents","tipo do present ao chamar set present ${presents}")
        present.value=presents
        Log.i("presents","tipo do present  ao prasar para present no presemts vlasse ${present.value}")
    }

    fun fluxoDePresents()= flow<String>{
        while (true){
            emit(present.value)
            delay(3000)
        }
    }

    fun checarPresent(tipo:String):Boolean{
        return presents.containsKey(tipo)
    }



}

class DataStore(val context: Context,val acaoDeAtualizarOEqualizador:(tipo:String,bandasDeAudio:List<Int>,ganhos:List<Short>)->Unit={t,b,g->}):AuxilarMediaSecion{
    private val sharedreferrence=context.getSharedPreferences("equalizador",Context.MODE_PRIVATE)
    private val dataStore=context.dataStore
    private val chave= stringPreferencesKey("ValoresDeEqualizacao")
    private val json= Json
    private var job:Job?=Job()
    private val scope = CoroutineScope(Dispatchers.IO+job!!)
    private val valorSalvo= MutableStateFlow<ValoresDeEqualizacao>(ValoresDeEqualizacao("", listOf(), listOf()))
    val tipoDePresents =valorSalvo.map { it.tipo }
    val listaDeBandasDeAudio =valorSalvo.map { it.bandsaDeAudio }
    val listaDeGanhos =valorSalvo.map { it.ganhos }

    init {
        scope.launch {
            coletarValoresDeEqualizacao()
        }
    }


    suspend fun coletarValoresDeEqualizacao() {
        scope.launch {Log.i("Equalizador", "data store $dataStore")
            dataStore?.data?.map {
                val string = it[chave]
                Log.i("Equalizador", "string salva no data store $string")
                if (string == null) ValoresDeEqualizacao("", listOf(), listOf())
                else json.decodeFromString(ValoresDeEqualizacao.serializer(), string)
            }?.collect {
                valorSalvo.value = it
                Log.i("presents","valor apos deserializacao ${it.tipo},${it.ganhos},${it.bandsaDeAudio}")
                acaoDeAtualizarOEqualizador(it.tipo,it.bandsaDeAudio,it.ganhos)
            }
        }
    }

    fun salvarValoresDeEqualizacao(tipo: String, bandasDeAudio: List<Int>, ganhos: List<Short>){
        val valoresDeEqualizacao=ValoresDeEqualizacao(tipo,bandasDeAudio,ganhos)
        scope.launch {
            dataStore?.edit {
                it[chave] =
                    json.encodeToString(ValoresDeEqualizacao.serializer(), valoresDeEqualizacao)
            }
        }
    }



    override fun finalizar() {
        if(job!=null) job!!.cancel()
         job=null
    }
}

@Serializable
class ValoresDeEqualizacao(val tipo:String,val bandsaDeAudio: List<Int>,val ganhos:List<Short>)







