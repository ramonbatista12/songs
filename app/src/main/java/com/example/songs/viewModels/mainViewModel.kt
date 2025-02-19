package com.example.songs.viewModels

import android.util.Log
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.songs.servicoDemidia.ResultadosConecaoServiceMedia
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainViewModel(var estateService:MutableStateFlow<ResultadosConecaoServiceMedia> ):ViewModel() {
   private val job= Job()
    private val scope= viewModelScope
    private val permicaoNotificacao= MutableStateFlow(false)
    private val bigPlyer= MutableStateFlow(false)
    val _permicaoNotificacao=permicaoNotificacao.asStateFlow()
    private val permicaoLeitura= MutableStateFlow(false)
    val _permicaoLeitura=permicaoLeitura.asStateFlow()
    val dialoNotificacao= MutableStateFlow(if(permicaoNotificacao.value)false else true)
    val dialoLeitura= MutableStateFlow(if(permicaoLeitura.value)false else true)
    val _bigPlyer= bigPlyer.asStateFlow()
    val snackbarHostState= SnackbarHostState()
    private val  corBackGround= MutableStateFlow<Color>(Color.Unspecified)
    val _corBackGround=corBackGround.asStateFlow()
    val corDotextonoAppBar= MutableStateFlow(Color.Unspecified)
    val _corDotextonoAppBar=corDotextonoAppBar.asStateFlow()
    init {
        scope.launch(Dispatchers.IO) {
            estateService.collect{
                when(it){
                    is ResultadosConecaoServiceMedia.Conectado->{
                        snackbarHostState.showSnackbar(it.toString())}
                    is ResultadosConecaoServiceMedia.Desconectado->{
                        snackbarHostState.showSnackbar(it.toString())}
                    is ResultadosConecaoServiceMedia.Erro->{
                        snackbarHostState.showSnackbar(it.toString())}

                    }                    }
            }
        }

    fun mudarBigPlyer(){
        if(bigPlyer.value)bigPlyer.value=false
        else bigPlyer.value=true
    }


    fun mudarPermicaoLeitura(value:Boolean){
        permicaoLeitura.value=value
        dialoLeitura.value= if(value)false else true
    }
    fun mudarPermicaoNotificacao(value:Boolean){
        permicaoNotificacao.value=value
        dialoNotificacao.value=if(value)false else true
    }

    fun mudancaSolicitarPermicaoLaeitua(value:Boolean){
        if(!value){
            permicaoLeitura.value=false
            dialoLeitura.value=false}
        else {
            permicaoLeitura.value=true
            dialoLeitura.value=false
        }
    }

    fun mudancaSolicitarPermicaoNotificao(value:Boolean){
        if(value){
            permicaoNotificacao.value=false
            dialoNotificacao.value=false}
        else{
            permicaoNotificacao.value=true
            dialoNotificacao.value=false
        }
    }

   fun mudarCorBackGround(value:Color){

             corBackGround.value=value



   }

    fun mudarCorBackGroundEtexto(backGround:Color,textonoAppBar:Color){

            corBackGround.value =backGround
            corDotextonoAppBar.value=textonoAppBar



    }







    }



class FabricaMainViewmodel{
    fun factory(mutableStateFlow: MutableStateFlow<ResultadosConecaoServiceMedia>) = object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(mutableStateFlow) as T
        }
    }
}


