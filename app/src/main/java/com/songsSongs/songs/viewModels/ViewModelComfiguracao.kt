package com.songsSongs.songs.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.songsSongs.songs.servicoDemidia.Equalizacao.Equalizador
import com.songsSongs.songs.servicoDemidia.ResultadosConecaoServiceMedia

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViewModelComfiguracao(val conecao:MutableStateFlow<ResultadosConecaoServiceMedia>):ViewModel(){
    private val scop=viewModelScope
    private var job:Job?=null
    private val _bandasDeAudio:MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val _ganhos:MutableStateFlow<List<MutableStateFlow<Short>>> = MutableStateFlow(emptyList())
    private val _opcoesDeEqualizacao= MutableStateFlow<List<String>>(emptyList())
    private val _presentSelecionado= MutableStateFlow<String>("")
    val bandasDeAudio=_bandasDeAudio.asStateFlow()
    val opcoesDeEqualizacao=_opcoesDeEqualizacao.asStateFlow()
    val ganhos=_ganhos.asStateFlow()
    val presentSelecionado=_presentSelecionado.asStateFlow()
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

    suspend fun coletaDosFluxo(ponteiroParaEqualizador: Equalizador?){
        job=scop.launch {
             scop.launch {
                ponteiroParaEqualizador?.bandasDeAudio?.collect{
                    _bandasDeAudio.value=it.map {
                        
                        val valor =it/1000
                        val string=valor.toString()+"hz"
                        string
                    }
                }
            }
            scop.launch {
                ponteiroParaEqualizador?.listaDePresents?.collect{
                    _opcoesDeEqualizacao.value=it
                }
            }

            scop.launch {
                ponteiroParaEqualizador?.ganhos?.collect{
                    _ganhos.value=it
                }
            }

            scop.launch {
                ponteiroParaEqualizador?.presentSelecionado?.collect{
                    _presentSelecionado.value=it
                }
            }

        }
    }

    fun  equalizarBanda(banda:Int,ganho:Short){
            scop.launch {
                when (val conecao=conecao.value){
                    is ResultadosConecaoServiceMedia.Conectado->{
                        conecao.setvice.equalizador?.equalizarBanda(banda,ganho)
                    }
                    else ->{}
                  }
             }
        }

    fun setPresent(tipo:String){
        scop.launch {
            when (val conecao=conecao.value){
                is ResultadosConecaoServiceMedia.Conectado->{
                   conecao.setvice.equalizador?.setPresent(tipo)
                }
                else ->{}
            }

            }


    }

    fun salvarValoresDeEqualizacao(){
        scop.launch {
            when (val conecao=conecao.value) {
                is ResultadosConecaoServiceMedia.Conectado -> {
                    conecao.setvice.equalizador?.salvarValoresDeEqualizacao()
                }

                else -> {}
            }
        }
    }


    override fun onCleared() {
        if(job!=null)job?.cancel()
        job=null
    }

}

class fabricaViewModelComfiguracoes(){
    fun fabricar(conecao:MutableStateFlow<ResultadosConecaoServiceMedia>)=

    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModelComfiguracao(conecao) as T
        }
    }
}