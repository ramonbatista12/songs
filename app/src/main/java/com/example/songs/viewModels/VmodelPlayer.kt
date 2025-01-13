package com.example.songs.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.songs.servicoDemidia.HelperPalyerEstados
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

class VmodelPlayer(val estadoService: MutableStateFlow<ResultadosConecaoServiceMedia>):ViewModel() {
  private val duracao=MutableStateFlow(0f)
  private val tempoTotal=MutableStateFlow(0L)
  private val emreproducao=MutableStateFlow<PlayerResultado>(PlayerResultado.Parado)
  private val caregando=MutableStateFlow(false)
  private val scope= viewModelScope
  val _duracao=duracao.asStateFlow()
  val _emreproducao=emreproducao.asStateFlow()
  val _caregando=caregando.asStateFlow()
  val _tempoTotal=tempoTotal.asStateFlow()
  private var job:Job?=null


init {
     scope.launch {
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
       job=scope.launch {

         scope.launch {    ponteiro._tempoTotal.collect{
             tempoTotal.value=it
         } }

          scope.launch {
               ponteiro._tempoDereproducao.map {
                   it/ (tempoTotal.value*100f)
               }.collect{
                   duracao.value=it
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

       }


   }

  override fun onCleared() {
     if(job!=null) job!!.cancel()
      super.onCleared()
  }


}


class FabricaViewmodelPlyer(){
   fun fabricar( estadoService: MutableStateFlow<ResultadosConecaoServiceMedia>)=

    object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VmodelPlayer(estadoService=estadoService) as T
        }
    }
}