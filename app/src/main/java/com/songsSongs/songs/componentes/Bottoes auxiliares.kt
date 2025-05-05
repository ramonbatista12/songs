package com.songsSongs.songs.componentes

import androidx.collection.emptyLongSet
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.songsSongs.songs.R
import com.songsSongs.songs.navegacao.DestinosDENavegacao

@Composable
fun BotaoAleatorio(
    onClick: () -> Unit={}){

     OutlinedButton(onClick=onClick) {
         Text(text = "Aleatorio")
         Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null)
     }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun  BotoesDeSelelcaoOpcoesDePlyList(modifier: Modifier,
                                     seleconado:MutableState<Icones>,
                                     windowSizeClass: WindowSizeClass ,
                                     acaoNavegar:(DestinosDENavegacao)->Unit={},
                                     accaoDeVoultar:()->Unit={}){
    val fracao = remember { MedicoesBotoesAuxiliare()  }
    if(windowSizeClass.windowWidthSizeClass!=WindowWidthSizeClass.COMPACT)
    FlowRow(modifier = modifier, verticalArrangement = Arrangement.Center) {

      Icones.listBotoesParaSelecaoDePlyList.forEach {
          OutlinedButton(onClick = {
              seleconado.value =it
              acaoNavegar(it.rota)
          }, modifier = Modifier.fillMaxWidth(fraction = fracao.fracaoDosBotoes(windowSizeClass) )) {
              Icon(painter = painterResource(id = it.icone), contentDescription = null)
              Spacer(modifier = Modifier.padding(1.dp))
              Text(text = it.toString())
          }
          Spacer(modifier = Modifier.padding(2.dp))
      }


    }
    else {
        val scrol = rememberScrollState()
        Row(modifier = modifier.fillMaxWidth().horizontalScroll(scrol)) {
            Spacer(modifier = Modifier.padding(2.dp))
            Icones.listBotoesParaSelecaoDePlyList.forEach {
                OutlinedButton(onClick = {
                    seleconado.value =it
                    acaoNavegar(it.rota)
                }, modifier = Modifier) {
                    Icon(painter = painterResource(id = it.icone), contentDescription = null)
                    Spacer(modifier = Modifier.padding(1.dp))
                    Text(text = it.toString())
                }
                Spacer(modifier = Modifier.padding(2.dp))
        }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun previaDosBottoes(){
    Column {
        BotaoAleatorio()
    }
}