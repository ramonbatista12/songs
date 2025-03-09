package com.example.songs.componentes.paineis

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable

import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.media3.common.Label

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Comfigurracoes(modifier: Modifier=Modifier){

    Row(modifier = modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        SeletorDeBandas { com.example.songs.componentes.paineis.Label("0000") }


    }


}

@Composable
fun Label(texto:String){
    Text(text = texto)
}



@Composable
fun SeletorDeBandas(label:@Composable (texto:String)->Unit,){
    val arrayBandas = Array(size = 5, init = {0})
    val arraiasStados = Array(size = 5, init = { remember { mutableStateOf(0f) }})

        val  frequnciasDeBandas= @Composable {
            repeat(arrayBandas.size){
                Text(text = "${arrayBandas[it]}")
            }
        }
        val sliders = @Composable {
            repeat(arrayBandas.size){
                Slider(value = arraiasStados[it].value, onValueChange = {v->
                    arraiasStados[it].value=v
                }, modifier = Modifier.width(100.dp).height(10.dp))
            }
        }

     Layout(contents = listOf(sliders,frequnciasDeBandas),modifier = Modifier.fillMaxWidth(), measurePolicy ={
             (slidersMeasurables,frequnciasDeBandasMeasurables),constraints->
            val medidaDosSlides=slidersMeasurables.map { it.measure(Constraints(maxWidth = 200,
                                                                                minWidth = 150,
                                                                                maxHeight = 10,
                                                                                minHeight = 10  )) }
            val medidaDosLabels=frequnciasDeBandasMeasurables.map { it.measure(constraints) }
            val heigt = medidaDosSlides.first() .height *medidaDosSlides.size
            val width = medidaDosSlides.first() .width  + medidaDosSlides.first().width

      layout(height = heigt,width = width){
          var x =0
          var y =0
          medidaDosLabels.forEachIndexed{index,it->
              it.place(x,y)
              x+=10
              medidaDosSlides.get(index).place(x,y)
              y+=10
              x=0
          }

      }
     })


}

/*
*..graphicsLayer {
    rotationZ=270f
    transformOrigin = TransformOrigin(0f,0f)
}

}
* */

@Composable
fun AlmentoDEGraves(label: @Composable (texto:String)->Unit){
    Box{}
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun ComfigurracoesPreview(){
   Scaffold(modifier = Modifier.fillMaxSize()) {
      Box(Modifier.fillMaxSize().padding(it)){ Comfigurracoes()}
   }

}


