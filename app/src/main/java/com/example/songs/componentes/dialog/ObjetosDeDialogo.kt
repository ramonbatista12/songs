package com.example.songs.componentes.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.songs.R
/*
* responsavel por definir as opcoes da caixa de dialo
* que e aberta quando se clica nas opcoes do item da lista
* */
sealed class ObjetosDeDialogOpcoesItemsListaMusicas(val opcao:String, val icone:Int?=null, val icones: ImageVector? =null){
    object Compartilha:ObjetosDeDialogOpcoesItemsListaMusicas(opcao = "Compartilhar", icone = R.drawable.sharp_share_24 )
    object AdicionarAplyList:ObjetosDeDialogOpcoesItemsListaMusicas(opcao = "Adicionar a Playlist", icones = Icons.Rounded.AddCircle)
    object AdicionarRemoverDaPlylist:ObjetosDeDialogOpcoesItemsListaMusicas(opcao = "Remover", icones = Icons.Rounded.Delete)
    companion object{
        val lista= listOf(Compartilha,AdicionarAplyList)
    }

}

sealed class ObjetosDeDialogOpcoesPlyList(val opcao:String, val icone:Int?=null, val icones: ImageVector? =null){
    object Apagar:ObjetosDeDialogOpcoesPlyList(opcao = "Apagar", icone =null, icones = Icons.Rounded.Delete )
    object Editar:ObjetosDeDialogOpcoesPlyList(opcao = "Renomear", icone =null, icones = Icons.Rounded.Edit )


}