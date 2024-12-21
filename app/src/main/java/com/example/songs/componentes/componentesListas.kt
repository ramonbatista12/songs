package com.example.songs.componentes

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.songs.R


@Composable
fun ItemDaLista(){
    Row (horizontalArrangement = Arrangement.SpaceBetween ,modifier = Modifier.fillMaxWidth(1f).padding(10.dp)){

        Image(painter = painterResource(id = R.drawable.baseline_music_note_24), contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(15.dp)
        ).size(80.dp))
        Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp).fillMaxWidth(0.8f)) {
            Spacer(Modifier.padding(8.dp))
            Text("Nome da Musica", maxLines = 2,fontSize = 18.sp)
            Spacer(Modifier.padding(3.dp))
            Text("Nome do Artista",maxLines = 1,fontSize = 14.sp)

        }
        Column(verticalArrangement = Arrangement.Bottom) {
             Text(text = "00:00")
        }


    }
}

@Composable
fun ItemsListaColunas(){
    Column {
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24),
                                       contentDescription = null,
                                       modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        Row {
            Column{
                Text("Nome da Musica", maxLines = 2,fontSize = 18.sp)
                Text("Nome do Artista",maxLines = 1,fontSize = 14.sp)

            }

        }
    }
}

@Composable
fun ItemsAlbums(){
    Row(modifier = Modifier.padding(10.dp),horizontalArrangement = Arrangement.SpaceBetween) {
        Image(painter = painterResource(id = R.drawable.baseline_music_note_24), contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(15.dp)
        ).size(80.dp))
        Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp)){
            Spacer(Modifier.padding(8.dp))
            Text("Nome do Album", maxLines = 2,fontSize = 18.sp)
            Spacer(Modifier.padding(3.dp))
            Text("Nome do Artista",maxLines = 1,fontSize = 14.sp)

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewItemDaLista() {
    val grad= remember { mutableStateOf(true) }
   Scaffold (modifier=Modifier.fillMaxSize()) {
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



@Preview(showBackground = true)
@Composable
fun PreviewItemsAlbums() {
    Surface(modifier=Modifier.fillMaxSize()) {
        LazyColumn {
            items(10){
                ItemsAlbums()

            }

        }
    }}