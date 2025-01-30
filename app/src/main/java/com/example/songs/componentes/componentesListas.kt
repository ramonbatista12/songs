package com.example.songs.componentes

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import com.example.songs.R
import com.example.songs.repositorio.Album
import com.example.songs.repositorio.Artista
import com.example.songs.repositorio.ListaPlaylist
import com.example.songs.ui.theme.DarkPink
import com.example.songs.ui.theme.SongsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/*
* aqui estao os items de lista cada funcao de descreve como cada item deve ser mostrado
* esta presente nesse arquivo
* */

@Composable
fun ItemDaLista(modifier: Modifier=Modifier,item:MediaItem?){
    val imagem =remember { mutableStateOf<Bitmap?>(null) }
    val scop=rememberCoroutineScope()
    val context= LocalContext.current
    LaunchedEffect(Unit){
        scop.launch(Dispatchers.IO) {
            Log.d("corotinas","entrou corotina ${Thread.currentThread().name} operacao load tumbmail ${item?.mediaMetadata?.title } ${item?.mediaId}")
            try {
                val bitmap=getMetaData(item!!.mediaMetadata.artworkUri!!,item.mediaId!!.toLong(),context = context)
                imagem.value=bitmap
            }catch (e:Exception){
                imagem.value=null
            }

        }
    }
    DisposableEffect(Unit) {
        onDispose {
            imagem.value=null
             scop.cancel()} }
    Row (horizontalArrangement = Arrangement.SpaceBetween ,
         verticalAlignment = Alignment.CenterVertically,
         modifier = modifier.padding(10.dp)){
        if(imagem.value==null){
        Icon(painter = painterResource(id = R.drawable.baseline_music_note_24_darkpink), contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(15.dp)
        ).size(80.dp), tint = DarkPink)}
        else{
            val bitmap=imagem!!.value!!.asImageBitmap()
            Image(bitmap = bitmap,contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(10.dp)).size(80.dp))
        }

        Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp).fillMaxWidth(0.8f)) {
            Spacer(Modifier.padding(8.dp))
            Text(if(item==null) "Nome da musica" else item.mediaMetadata.title.toString(), maxLines = 2,fontSize = 18.sp)
            Spacer(Modifier.padding(3.dp))
            Text(if(item==null)"nome do artista" else item.mediaMetadata.artist.toString(),maxLines = 1,fontSize = 14.sp)

        }



    }
}

@RequiresApi(Build.VERSION_CODES.Q)
suspend  fun getMetaData(uri: Uri, id: Long,context: Context):Bitmap?{
    Log.d("Metadata loaad tumb","id de media ${id}")
    try {
        val resolver = context.contentResolver
        val tumbmail=resolver.loadThumbnail(uri, Size(400,400),null)
        return tumbmail
    }catch (e:Exception){
        return null
    }

}

@Composable
fun ItemsListaColunas(modifier: Modifier=Modifier,item:MediaItem?=null){

    val imagem =remember { mutableStateOf<Bitmap?>(null) }
    val scop=rememberCoroutineScope()
    val context= LocalContext.current
    LaunchedEffect(Unit){
        scop.launch(Dispatchers.IO) {
            try {
                val bitmap=getMetaData(item!!.mediaMetadata.artworkUri!!,item.mediaId!!.toLong(),context = context)
                imagem.value=bitmap
            }catch (e:Exception){
                imagem.value=null
            }

        }
    }
    DisposableEffect(Unit) {
        onDispose {
            imagem.value=null
            scop.cancel()
        }
    }
    Column(modifier = modifier.wrapContentWidth()) {
        if(imagem.value==null)
        Icon(painter = painterResource(id = R.drawable.baseline_music_note_24),
                                       contentDescription = null,
                                       modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp), tint = DarkPink)
        else{
            val bitmap=imagem!!.value!!.asImageBitmap()
            Image(bitmap = bitmap,contentDescription = null,modifier = Modifier.clip(
                RoundedCornerShape(10.dp)).size(80.dp))
        }

        Spacer(Modifier.padding(8.dp))
        Row {
            Column{
                Text(text=if(item==null) "Nome da musica" else item.mediaMetadata.title.toString(),
                     maxLines = 2,fontSize = 18.sp,
                     modifier = Modifier.fillMaxWidth(0.6f))
                Spacer(Modifier.padding(8.dp))
                Text(if (item==null) "Nome do Artista" else item.mediaMetadata.artist.toString(),maxLines = 1,fontSize = 14.sp)

            }

        }
    }
}

@Composable
fun ItemsAlbums(modifier: Modifier=Modifier,item: Album){
    val imagem =remember { mutableStateOf<Bitmap?>(null) }
    val scop=rememberCoroutineScope()
    val context= LocalContext.current
    LaunchedEffect(Unit){
        scop.launch(Dispatchers.IO) {
            Log.d("corotinas","entrou corotina ${Thread.currentThread().name} operacao load tumbmail ${item.nome } ${item.idDoalbum}")
            try {
                imagem.value= getMetaData(uri = item.uri,id = item.idDoalbum.toLong(),context = context)
            }catch (e:Exception){
                imagem.value=null
            }
        }

    }
    Row(modifier = modifier.padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        if (imagem.value==null)
        Image(painter = painterResource(id = R.drawable.baseline_album_24), contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(15.dp)
        ).size(80.dp))
        else{
            val bitmap=imagem!!.value!!.asImageBitmap()
            Image(bitmap = bitmap,contentDescription = null,modifier = Modifier.clip(
                RoundedCornerShape(15.dp)
            ).size(80.dp))}
        Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp)){
            Spacer(Modifier.padding(8.dp))
            Text(item.nome, maxLines = 2,fontSize = 18.sp, fontFamily = FontFamily.Monospace)
            Spacer(Modifier.padding(3.dp))
            Text(item.artista,maxLines = 1,fontSize = 14.sp)

        }
    }
    DisposableEffect(Unit) {
        onDispose {
            imagem.value=null
            scop.cancel()
        }
    }
}

@Composable
fun ItemsAlbusColuna(modifier: Modifier=Modifier,item: Album){
    val context= LocalContext.current
    val scop=rememberCoroutineScope()
    val bitmap= remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(Unit) {
        scop.launch(Dispatchers.IO) {
            Log.d("corotinas","entrou corotina ${Thread.currentThread().name} operacao load tumbmail ${item.nome } ${item.idDoalbum}")
            try {
                    bitmap.value= getMetaData(item.uri,item.idDoalbum.toLong(),context)
                }catch (e:Exception){
                    bitmap.value=null
                }
        }

    }
    DisposableEffect(Unit) {
        onDispose {
            bitmap.value=null
            scop.cancel()
        }
    }
    Column(modifier =modifier.padding(10.dp)) {
        if(bitmap.value==null)
        Image(painter = painterResource(id = R.drawable.baseline_album_24),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        else{
            val _bitmap=bitmap!!.value!!.asImageBitmap()
            Image(bitmap=_bitmap,
                contentDescription = null,
                modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        }
        Row {
            Column{
                Text(item.nome, maxLines = 2,fontSize = 18.sp)
                Text(item.artista,maxLines = 1,fontSize = 14.sp)

            }

        }
    }
}
@Composable
fun ItemsArtistas(modifier: Modifier=Modifier,item:Artista){
    Row(modifier = modifier.padding(10.dp),horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Image(painter = painterResource(id = R.drawable.baseline_artistas_24), contentDescription = null,modifier = Modifier.clip(
            RoundedCornerShape(15.dp)
        ).size(80.dp))
        Column(horizontalAlignment = Alignment.Start,modifier = Modifier.padding(10.dp)){
            Spacer(Modifier.padding(8.dp))
            Text(item.nome, maxLines = 2,fontSize = 18.sp, color = DarkPink)
            Spacer(Modifier.padding(3.dp))
           // Text("Nome do Artista",maxLines = 1,fontSize = 14.sp, color = DarkPink)

        }
    }
}

@Composable
fun ItemsArtistasColuna(modifier: Modifier=Modifier,item: Artista){

    Column(modifier =modifier.padding(10.dp)) {
        Image(painter = painterResource(id = R.drawable.baseline_artistas_24),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        Row {
            Column{
                Text(item.nome, maxLines = 2,fontSize = 18.sp)
               // Text("Nome do Artista",maxLines = 1,fontSize = 14.sp)

            }

        }
    }
}




@Composable
fun ItemsListaPlaylists(modifier: Modifier=Modifier,item:ListaPlaylist){
    Column(modifier =modifier.padding(10.dp).wrapContentSize()) {
        Image(painter = painterResource(id = R.drawable.baseline_playlist_play_24),
            contentDescription = null,
            modifier = Modifier.clip(RoundedCornerShape(15.dp)).size(80.dp))
        Row {
            Column{
                Text(item.nome, maxLines = 2,fontSize = 18.sp)


            }

        }
    }

}





@Preview(showBackground = true)
@Composable
fun PreviewItemDaLista() {
    SongsTheme {
     val grad= remember { mutableStateOf(true) }
   Scaffold (modifier=Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background).safeContentPadding().safeGesturesPadding().safeDrawingPadding()) {
       val teste = MaterialTheme.colorScheme.background.toString()
       LaunchedEffect(Unit) {
          Log.i("teste cor",teste)
       }
    Box(modifier = Modifier.padding(it)) {
       LazyVerticalGrid(columns = GridCells.Fixed(if(grad.value) 3 else 1),horizontalArrangement = Arrangement.Center, modifier = Modifier.align(
           Alignment.Center)) {
       if(grad.value)   items(5){
                      LoadingListaMusicasColunas()
                            }
       else items(10){
           LoadingListaMusicasColunas()
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
                LoadingListaMusicasColunas()

            }

        }
    }}