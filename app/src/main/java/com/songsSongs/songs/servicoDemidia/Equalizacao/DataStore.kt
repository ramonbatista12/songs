package com.songsSongs.songs.servicoDemidia.Equalizacao

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.songsSongs.songs.application.AplicationCuston.conteiner.dataStore
import com.songsSongs.songs.servicoDemidia.AuxilarMediaSecion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class DataStore(val context: Context, val acaoDeAtualizarOEqualizador:(tipo:String, bandasDeAudio:List<Int>, ganhos:List<Short>)->Unit={ t, b, g->}):
    AuxilarMediaSecion {
    private val sharedreferrence=context.getSharedPreferences("equalizador", Context.MODE_PRIVATE)
    private val dataStore=context.dataStore
    private val chave= stringPreferencesKey("ValoresDeEqualizacao")
    private val json= Json
    private var job: Job?= Job()
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

            Log.i("Equalizador", "data store $dataStore")
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



    fun salvarValoresDeEqualizacao(tipo: String, bandasDeAudio: List<Int>, ganhos: List<Short>){
        val valoresDeEqualizacao= ValoresDeEqualizacao(tipo,bandasDeAudio,ganhos)
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