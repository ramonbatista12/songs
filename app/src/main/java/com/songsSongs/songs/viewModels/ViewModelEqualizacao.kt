package com.songsSongs.songs.viewModels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.songsSongs.songs.servicoDemidia.Equalizador
import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewModelEqualizacao(val conecao:MutableStateFlow<ResultadosConecaoServiceMedia>):ViewModel(){
    private val scop=viewModelScope
    private var job:Job?=null
    private val graves= MutableStateFlow(0)
    private val medios= MutableStateFlow(0)
    private val agudos= MutableStateFlow(0)
    val gravesString=graves.asStateFlow()
    val mediosString=medios.asStateFlow()
    val agudosString=agudos.asStateFlow()
    init {
        scop.launch {
            conecao.collect{
                when(it){
                    is ResultadosConecaoServiceMedia.Conectado->{
                        coletaDosFluxo(it.setvice.equalizador)

                    }
                    else ->{
                        if(job!=null)job?.cancel()
                        job=null
                    }
            }
          }
        }
    }

    suspend fun coletaDosFluxo(ponteiroParaEqualizador:Equalizador?){
        job=scop.launch {

           scop.launch {
               ponteiroParaEqualizador?.agudos?.collect{
                   agudos.value=it.toInt()
               }
           }
           scop.launch {
               ponteiroParaEqualizador?.medios?.collect{
                   medios.value=it.toInt()
               }
           }
           scop.launch {
               ponteiroParaEqualizador?.graves?.collect{
                   graves.value=it.toInt()
               }
           }

        }
    }

    fun equalizarGraves(ganho:Short){
        if(ganho<-15||ganho>15)throw RuntimeException("ganho ultapasa o valor permitido")
        scop.launch {
            when (val conecao=conecao.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    conecao.setvice.equalizador?.equalizarGraver(ganho)
                }
                else ->{}

            }
        }
    }

    fun equalizarMedios(ganho:Short){
        if(ganho<-15||ganho>15)throw RuntimeException("ganho ultapasa o valor permitido")
        scop.launch {
            when (val conecao=conecao.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    conecao.setvice.equalizador?.equalizarMedios(ganho)
                }
                else ->{}

            }
        }
    }
    fun equalizarAgudos(ganho:Short){
        if(ganho<-15||ganho>15)throw RuntimeException("ganho ultapasa o valor permitido")
        scop.launch {
            when (val conecao=conecao.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                    conecao.setvice.equalizador?.equalizarAgudos(ganho)
                }
                else ->{}

            }
        }
    }

    override fun onCleared() {
       if(job!=null)job?.cancel()
        job=null

    }

}