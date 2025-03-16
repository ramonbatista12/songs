package com.songsSongs.songs.componentes.paineis

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout

import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Comfigurracoes(modifier: Modifier=Modifier){

    Row(modifier = modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        SeletorDeBandas { com.songsSongs.songs.componentes.paineis.Label("0000") }


    }


}

@Composable
fun Label(texto:String){
    Text(text = texto)
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeletorDeBandas(label:@Composable (texto:String)->Unit,){
    val arrayBandas = Array(size = 12, init = {0})
    val arraiasStados = Array(size = 12, init = { remember { mutableStateOf(0f) }})
        val context =LocalContext.current
        val  frequnciasDeBandas= @Composable {
            repeat(arrayBandas.size){
                Text(text = "${arrayBandas[it]}",modifier=Modifier.width(70.dp).border(width = 1.dp, color = androidx.compose.ui.graphics.Color.Black))
            }
        }
        val sliders = @Composable {
            repeat(arrayBandas.size){
                val interacao =remember { MutableInteractionSource() }
                Slider(value = arraiasStados[it].value,
                       onValueChange = { },
                       thumb = {SliderDefaults.Thumb(interactionSource = interacao,modifier = Modifier.size(15.dp))},
                       track = {SliderDefaults.Track(sliderState = it,modifier = Modifier.height(5.dp))},

                       modifier = Modifier.width(10.dp).height(100.dp).graphicsLayer {
                           rotationZ=270f
                           transformOrigin = TransformOrigin(0f,0f)
                       }.layout(
                           {measurables,constraints->
                               val m =measurables.measure(Constraints(maxWidth = constraints.maxHeight,
                                                                      minWidth = constraints.minHeight,
                                                                      maxHeight = constraints.maxWidth,
                                                                      minHeight = constraints.minWidth))
                               layout(m.height,m.width){
                                   Log.d("teste","altura do slide ${m.height}, largura ${m.width}")
                                   m.place(-m.width,0)
                               }
                           }
                       )
                    )
                }
            }


     Layout(contents = listOf(sliders,frequnciasDeBandas),modifier = Modifier.border(width = 1.dp, color = androidx.compose.ui.graphics.Color.Black), measurePolicy ={
             (slidersMeasurables,frequnciasDeBandasMeasurables),constraints->
            val medidaDosSlides=slidersMeasurables.map { it.measure(constraints) }
            val medidaDosLabels=frequnciasDeBandasMeasurables.map { it.measure(constraints) }
            val heigt = medidaDosSlides.first().height +medidaDosLabels.first().height+20
            val width = (medidaDosLabels.first().width*medidaDosLabels.size  )+(10*medidaDosLabels.size)

      layout(height = heigt,width = width){
          var x =0//marcara o final do desenho no eixo x
          var y =0
          var xAux=medidaDosLabels.first().width //macara a posicao do desenho no eixo x
          medidaDosSlides.forEachIndexed{index,it->
              xAux=x+(medidaDosLabels.first().width/2)
              it.place(xAux,y)

              x+=10+medidaDosLabels.first().width
              Log.d("teste", "altura do slide no posicionamento altura: ${it.height}, largura :${it.width}, valor do eixo x: $x")
          }
          y=medidaDosSlides.first().height+20
          x =0

          medidaDosLabels.forEachIndexed{index,it->
              it.place(x,y)
              x+=(10+it.width)


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
      Box(Modifier.fillMaxWidth(0.8f).padding(it)){ Comfigurracoes()}
   }

}


