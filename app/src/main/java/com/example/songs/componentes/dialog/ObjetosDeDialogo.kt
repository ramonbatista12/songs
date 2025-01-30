package com.example.songs.componentes.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.songs.R
/*
* responsavel por definir as opcoes da caixa de dialo
* que e aberta quando se clica nas opcoes do item da lista
* */
sealed class ObjetosDeDialog(val opcao:String, val icone:Int?=null, val icones: ImageVector? =null){
    object Compartilha:ObjetosDeDialog(opcao = "Compartilhar", icone = R.drawable.sharp_share_24 )
    object AdicionarAplyList:ObjetosDeDialog(opcao = "Adicionar a Playlist", icones = Icons.Rounded.AddCircle)
    companion object{
        val lista= listOf(Compartilha,AdicionarAplyList)
    }

}

