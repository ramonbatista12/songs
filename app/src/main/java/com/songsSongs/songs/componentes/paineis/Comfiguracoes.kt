package com.songsSongs.songs.componentes.paineis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.songsSongs.songs.R
import com.songsSongs.songs.componentes.MedicoesItemsDeList

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Comfigurracoes(modifier: Modifier=Modifier,windowSizeClass: WindowSizeClass){
    val auxiliarMedicoes= remember { MedicoesItemsDeList() }
    LazyVerticalGrid(modifier=modifier,
                     columns = GridCells.Fixed(auxiliarMedicoes.gradCell(windowSizeClass)),
                     horizontalArrangement = Arrangement.SpaceBetween ) {
         ItemsDeComfiguracoes.list.forEach{
             item {
                 CardComfiguracoes(texto = it.texto, iconeId = it.iconeId, icone = it.icone)
             }
         }
    }


}


sealed class ItemsDeComfiguracoes(val texto:String,val iconeId:Int?,val icone:ImageVector?){
          object equalizacao:ItemsDeComfiguracoes(texto = "Equalizacao",iconeId = R.drawable.iconeequalizacao2,null)
          object playlist:ItemsDeComfiguracoes(" Lista De Musicas",iconeId = R.drawable.iconecomfiguracoesdelista,null)

    companion object{
        val list= listOf(equalizacao,playlist)
    }

}

@Composable
fun CardComfiguracoes(texto:String, iconeId:Int?=null, icone:ImageVector?=null){
     OutlinedButton(onClick = { /*TODO*/ }) {
         Row {
          val onbackgraund =MaterialTheme.colorScheme.onBackground
             Text(text = texto)
             Spacer(Modifier.padding(3.dp))
             if(iconeId!=null)
                 Icon(painter = painterResource(id = iconeId), contentDescription = texto,tint=onbackgraund,)


             }

     }
}

@Composable
fun Label(texto:String){
    Text(text = texto)
}





