package com.songsSongs.songs.componentes.paineis

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

import com.songsSongs.songs.ui.theme.DarkPink
import com.songsSongs.songs.viewModels.ViewModelComfiguracao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Equalizador(m:Modifier=Modifier,vm: ViewModelComfiguracao,windowSizeClass: WindowSizeClass){

EqualizadorCompat(m,vm,windowSizeClass)

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EqualizadorCompat(m: Modifier,vm: ViewModelComfiguracao,windowSizeClass: WindowSizeClass){
    val opcoesDeEqualizacao=vm.opcoesDeEqualizacao.collectAsState()
    val bandasDeAudio=vm.bandasDeAudio.collectAsState()
    val ganhos=vm.ganhos.collectAsState()
    val  scroll = rememberScrollState()
    val selecionado=vm.presentSelecionado.collectAsState()
    Column(horizontalAlignment =  Alignment.CenterHorizontally,
           verticalArrangement =  if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) Arrangement.Top else Arrangement.Center,
        modifier = m.verticalScroll(state = scroll)) {
        FlowRow(horizontalArrangement = Arrangement.Start) {
            opcoesDeEqualizacao.value.forEach {



                OutlinedButton(onClick = {

                    vm.setPresent(it)

                }, colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = if (selecionado.value == it) DarkPink else MaterialTheme.colorScheme.onBackground,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = MaterialTheme.colorScheme.onBackground
                    ) ) {
                    Text(text = it)
                }
            }
        }
        FlowRow(modifier = m.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
           if(!bandasDeAudio.value.isEmpty()&&!ganhos.value.isEmpty()){
               bandasDeAudio.value.forEachIndexed { index, s ->
                   Seletor(tipoFrequencia = bandasDeAudio.value[index], acaoDeGanho = {
                       if(selecionado.value=="custon")
                       vm.equalizarBanda(index,it.toShort())
                   }, estadoDganho = ganhos.value[index])
               }
           }
           repeat(bandasDeAudio.value.size){indx->

           }


        }}
    DisposableEffect(Unit) {
        onDispose {
         vm.salvarValoresDeEqualizacao()
        }
    }

}

@Composable
private fun EqualizadorExtendido(m: Modifier,vm: ViewModelComfiguracao,windowSizeClass: WindowSizeClass){


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Seletor(tipoFrequencia:String="Graves",acaoDeGanho:(Short)->Unit,valorDaBanda:Short=0,estadoDganho:MutableStateFlow<Short>){
    val valor=estadoDganho.collectAsState()




Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center ,modifier = Modifier.height(250.dp).width(70.dp)) {
val estado=remember{ mutableStateOf(0f) }
val interactionSource =remember { MutableInteractionSource() }
val decibels = remember { mutableStateOf(0) }
    val scope=rememberCoroutineScope()
Box(modifier = Modifier.rotate(270f).size(120.dp,50.dp)){
Slider(modifier = Modifier,
value = valor.value.toFloat()/100f,
onValueChange = {
    scope.launch {
        acaoDeGanho(it.toInt().toShort())
        Log.i("Slider","valor do slider $it")
    }


},
thumb = {SliderDefaults.Thumb(modifier = Modifier.size(10.dp,20.dp), interactionSource = interactionSource)},
track = {SliderDefaults.Track(it,modifier = Modifier.height(5.dp).clip(shape = RectangleShape))},
valueRange = -15f..15f)
}


Spacer(Modifier.padding(10.dp))
Text("${valor.value.toInt()/100} dB")
Text(text = tipoFrequencia, maxLines = 1)


}

}

