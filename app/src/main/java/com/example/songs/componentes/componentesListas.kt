package com.example.songs.componentes

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.songs.R
import com.example.songs.ui.theme.DarkPink
import com.example.songs.ui.theme.SongsTheme


@Composable
fun ItemDaLista(modifier: Modifier=Modifier){
    Row (horizontalArrangement = Arrangement.SpaceBetween ,modifier = modifier.padding(10.dp)){

        Icon(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink), contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(15.dp)
        ).size(80.dp), tint = DarkPink)
        Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp).fillMaxWidth(0.8f)) {
            Spacer(Modifier.padding(8.dp))
            Text("Nome da Musica", maxLines = 2,fontSize = 18.sp)
            Spacer(Modifier.padding(3.dp))
            Text("Nome do Artista",maxLines = 1,fontSize = 14.sp)

        }



    }
}

@Composable
fun ItemsListaColunas(modifier: Modifier=Modifier){
    Column(modifier = modifier.wrapContentWidth()) {
        Icon(painter = painterResource(id = R.drawable.baseline_music_note_24),
                                       contentDescription = null,
                                       modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp), tint = DarkPink)
        Row {
            Column{
                Text("Nome da Musica", maxLines = 2,fontSize = 18.sp)
                Text("Nome do Artista",maxLines = 1,fontSize = 14.sp)

            }

        }
    }
}

@Composable
fun ItemsAlbums(modifier: Modifier=Modifier){
    Row(modifier = modifier.padding(10.dp),horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink), contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(15.dp)
        ).size(80.dp))
        Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp)){
            Spacer(Modifier.padding(8.dp))
            Text("Nome do Album", maxLines = 2,fontSize = 18.sp, color = DarkPink)
            Spacer(Modifier.padding(3.dp))
            Text("Nome do Artista",maxLines = 1,fontSize = 14.sp, color = DarkPink)

        }
    }
}

@Composable
fun ItemsAlbusColuna(modifier: Modifier=Modifier){
    Column(modifier =modifier.padding(10.dp).wrapContentSize()) {
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        Row {
            Column{
                Text("Nome da Album", maxLines = 2,fontSize = 18.sp)
                Text("Nome do Artista",maxLines = 1,fontSize = 14.sp)

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewItemDaLista() {
    SongsTheme {
     val grad= remember { mutableStateOf(true) }
   Scaffold (modifier=Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
       val teste = MaterialTheme.colorScheme.background.toString()
       LaunchedEffect(Unit) {
          Log.i("teste cor",teste)
       }
    Box(modifier = Modifier.padding(it)) {
       LazyVerticalGrid(columns = GridCells.Fixed(if(grad.value) 2 else 1),horizontalArrangement = Arrangement.Center, modifier = Modifier.align(
           Alignment.Center)) {
       if(grad.value)   items(10){
                        ItemsListaColunas()
                            }
       else items(10){
           ItemDaLista()
       }


      }
   }
   }

    }
}



@Preview(showBackground = true)
@Composable
fun PreviewItemsAlbums() {
    Surface(modifier=Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.background(color = Color.Black)) {
            items(10){
                ItemsAlbums()

            }

        }
    }}