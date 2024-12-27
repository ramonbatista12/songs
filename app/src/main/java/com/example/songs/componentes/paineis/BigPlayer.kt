package com.example.songs.componentes.paineis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.songs.R
import com.example.songs.componentes.BarraSuperio
import com.example.songs.componentes.ItemDaLista
import com.example.songs.ui.theme.DarkPink
import com.example.songs.ui.theme.SongsTheme


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BigPlayer(modifier: Modifier = Modifier,windowSizeClass: WindowSizeClass){
    val  navegacao = rememberListDetailPaneScaffoldNavigator<Long>( )
  if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT)
      PlayerCompat()

 else if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM) PlayerCompat()
    else  PlyerEspandido(Modifier,windowSizeClass)





}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Player(modifier: Modifier=Modifier){
    Column(modifier =modifier.padding(10.dp).background(color = MaterialTheme.colorScheme.background)) {

        Icon(painter = painterResource(id = R.drawable.baseline_music_note_24),modifier = Modifier.size(400.dp).clip(
            RoundedCornerShape(15.dp)
        ).border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(15.dp)), contentDescription = null, tint = DarkPink)
        Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier) {

            Text(text = "Nome da Musica")
            Spacer(Modifier.padding( 8.dp))
            Text(text = "Nome do Artista")
            Spacer(Modifier.padding(10.dp))
        Column(modifier = Modifier.width(400.dp)) {
            var range= remember {  mutableStateOf<Float>(0f)}
             Slider(value = range.value, onValueChange = {
                 range.value=it
             },colors = SliderDefaults.colors(activeTrackColor = DarkPink), valueRange = 0f..1f)
            Spacer(Modifier.padding(10.dp))

            Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
                Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = DarkPink)
                Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = DarkPink)
                Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = null,tint = DarkPink)
                Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint = DarkPink)
                Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24), contentDescription = null, tint = DarkPink)

            }
        }
        }


    }
}

@Composable
fun PlayerCompat(){
    Box(modifier = Modifier.fillMaxSize().imePadding()){
        Player(Modifier.align(Alignment.Center))

        IconButton({},modifier = Modifier.align(Alignment.BottomCenter).size(70.dp)) {
            Icon(painter = painterResource(id = R.drawable.baseline_list_24), contentDescription = null, tint = DarkPink, modifier = Modifier.align(
                Alignment.BottomCenter).size(50.dp))

        }
    }



}


@Composable
fun PlyerEspandido(modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass){
     val progresso = remember { mutableStateOf(0f) }
    Row (modifier.fillMaxSize().padding(top = 70.dp, bottom = 10.dp)){
        Box(Modifier.fillMaxWidth(0.5f).clip(RoundedCornerShape(15.dp)).padding(10.dp)){

            Column(Modifier.align(Alignment.Center)) {
                    val iconeSize =if(windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 90.dp else 400.dp
                    Icon(painter = painterResource(R.drawable.baseline_music_note_24),
                        contentDescription = null ,
                        modifier = Modifier.size(iconeSize).clip(
                        RoundedCornerShape(15.dp))
                                           .border(width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(15.dp)).align(Alignment.CenterHorizontally), tint = Color.Black)

                    Column {

                        Text(text = "Nome da Musica")
                        Spacer(Modifier.padding(3.dp))
                        Text(text = "Nome do Artista")
                        Spacer(Modifier.padding(3.dp))
                      Column {
                          Slider(value = progresso.value, onValueChange = {progresso.value=it}, colors = SliderDefaults.colors(activeTrackColor = DarkPink), modifier = Modifier.fillMaxWidth())
                          Spacer(Modifier.padding(3.dp))
                          Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween){
                              Icon(painter = painterResource(id = R.drawable.baseline_skip_previous_24), contentDescription = null,tint = DarkPink)
                              Icon(painter = painterResource(id = R.drawable.baseline_shuffle_24), contentDescription = null,tint = DarkPink)
                              Icon(painter = painterResource(id = R.drawable.baseline_play_arrow_24), contentDescription = null,tint = DarkPink)
                              Icon(painter = painterResource(id = R.drawable.baseline_repeat_24), contentDescription = null,tint = DarkPink)
                              Icon(painter = painterResource(id = R.drawable.baseline_skip_next_24), contentDescription = null, tint = DarkPink)

                          }
                      }


                    }

            }

        Spacer(Modifier.padding(10.dp))

        }
        Box(Modifier.fillMaxWidth(0.5f).clip(RoundedCornerShape(15.dp)).border(0.5.dp, color = DarkPink, shape = RoundedCornerShape(15.dp))){
            LazyColumn(Modifier.align(Alignment.Center).fillMaxWidth(1f)) {
                items(80){
                    ItemDaLista()
                }

            }



        }



    }




}






@Preview(showBackground = true)
@Composable
fun PlayerPreview(){
    SongsTheme {
        Surface {
        Scaffold(topBar = { BarraSuperio(titulo = "Plyer") }, modifier = Modifier.safeDrawingPadding().safeGesturesPadding().safeContentPadding()) {

            val windowsizeclass = currentWindowAdaptiveInfo().windowSizeClass
            BigPlayer(modifier = Modifier.padding(it),windowSizeClass = windowsizeclass)

        }
        }
    }

    }
