package com.songsSongs.songs.componentes.paineis

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Equalizador(m:Modifier=Modifier){
 FlowRow(modifier = m.fillMaxWidth()) {
  Seletor(tipoFrequencia = "Graves")
  Seletor(tipoFrequencia = "Medios")
  Seletor(tipoFrequencia = "Agudos")
  Seletor(tipoFrequencia = "Agudos")
  Seletor(tipoFrequencia = "Agudos")

 }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Seletor(tipoFrequencia:String="Graves"){

  Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center ,modifier = Modifier.height(250.dp).width(70.dp)) {
     val estado=remember{ mutableStateOf(0f) }
     val interactionSource =remember { MutableInteractionSource() }
     val decibels = remember { mutableStateOf(0) }
      Box(modifier = Modifier.rotate(270f).size(120.dp,50.dp)){
          Slider(modifier = Modifier,
          value = estado.value,
          onValueChange = {
              estado.value=it
              decibels.value=it.toInt()
              Log.i("Slider","valor do slider $it")
          },
          thumb = {SliderDefaults.Thumb(modifier = Modifier.size(10.dp,20.dp), interactionSource = interactionSource)},
          track = {SliderDefaults.Track(it,modifier = Modifier.height(5.dp).clip(shape = RectangleShape))},
          valueRange = -15f..15f)
      }


   Spacer(Modifier.padding(10.dp))
   Text("${decibels.value} dB")
   Text(text = tipoFrequencia)


  }

}

