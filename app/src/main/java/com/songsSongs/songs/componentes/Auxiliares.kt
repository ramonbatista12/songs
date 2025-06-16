package com.songsSongs.songs.componentes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

import android.os.Build
import android.util.Log
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

class AuxiliarMudancaDeBackGrands{
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun mudarBackgrandScaffold(bitmap: Bitmap?,
                               acao:(backgraudColor:androidx.compose.ui.graphics.Color)->Unit,
                               backgraudColor: Color,cor:MutableState<androidx.compose.ui.graphics.Color>,
                               corTexto:MutableState<androidx.compose.ui.graphics.Color>,textColorSquemas:androidx.compose.ui.graphics.Color){


        if(bitmap!=null){
            val palette = Palette.from(bitmap).generate()
            val int:Int
            if(palette.darkMutedSwatch!=null){
                Log.d("backgrand","${palette.darkMutedSwatch}")
                int=palette.getDarkMutedColor(backgraudColor.value.toInt())}
            else if(palette.mutedSwatch!=null){
                Log.d("backgrand","${palette.mutedSwatch}")
                int=palette.getMutedColor(backgraudColor.value.toInt())}
            else if(palette.dominantSwatch!=null){
                int=palette.getDominantColor(backgraudColor.value.toInt())}
            else
                int=backgraudColor.value.toInt()




            cor.value= Color(int)

            val luminessenciaBackgraud=cor.value.luminance()
            Log.d("Luminecencia","${luminessenciaBackgraud}")
            corTexto.value=when{
                (luminessenciaBackgraud == 0.0f)-> textColorSquemas
                (luminessenciaBackgraud>0.0f&&luminessenciaBackgraud<0.1f) -> androidx.compose.ui.graphics.Color.White
                (luminessenciaBackgraud>=0.1f) -> androidx.compose.ui.graphics.Color.Black

                else -> androidx.compose.ui.graphics.Color.Unspecified
            }

    }else{
        cor.value=backgraudColor
        corTexto.value=textColorSquemas
        }
        acao(cor.value)
    }
    @SuppressLint("SuspiciousIndentation")
    suspend fun mudarBackgrandMiniPlyer(bitmap: Bitmap?,
                                        corTexto:MutableState<Color>,
                                        backgraudColor:MutableState<Color>,
                                        textColorSquemas: Color,
                                        backgraudColorSquemas: Color){
        if(bitmap!=null){
            Log.d("bitmap mini plyer","${bitmap}")
            val palette =Palette.from(bitmap).generate()

            val int:Int
            if(palette.darkMutedSwatch!=null)
                int=palette.getDarkMutedColor(backgraudColorSquemas.value.toInt())
            else if(palette.mutedSwatch!=null)
                int=palette.getMutedColor(backgraudColorSquemas.value.toInt())
            else if(palette.dominantSwatch!=null)
                int=palette.getDominantColor(backgraudColorSquemas.value.toInt())
            else
                int=backgraudColor.value.value.toInt()
                backgraudColor.value=Color(int)
            val luminessenciaBackgraud=backgraudColor.value.luminance()
            corTexto.value=when{
                (luminessenciaBackgraud == 0.0f)-> textColorSquemas
                (luminessenciaBackgraud>0.0f&&luminessenciaBackgraud<0.1f) ->Color.White
                (luminessenciaBackgraud>=0.1f) ->Color.Black

                else -> Color.Unspecified
            }

        }
        else {
            backgraudColor.value=backgraudColorSquemas
            corTexto.value=textColorSquemas

        }

    }
    suspend fun mudaBackgraundScafolldPermanentBar(bitmap: Bitmap?,
                                                   acaoMudarCorScafollEBArraPermanente:(backgrand:Color,corBarra:Color)->Unit,
                                                   corBackGraund:MutableState<Color>,
                                                   corTexto:MutableState<Color>,textColorSquemas: Color,backgraudColorSquemas:Color){
        if(bitmap!=null){
            val palette =Palette.from(bitmap).generate()
            val int:Int
            if(palette.darkMutedSwatch!=null)
                int=palette.getDarkMutedColor(backgraudColorSquemas.value.toInt())
            else if(palette.mutedSwatch!=null)
                int=palette.getMutedColor(backgraudColorSquemas.value.toInt())
            else if(palette.dominantSwatch!=null)
                int=palette.getDominantColor(backgraudColorSquemas.value.toInt())
            else
                int=backgraudColorSquemas.value.toInt()
            corBackGraund.value=Color(int)

            val luminessenciaBackgraud=corBackGraund.value.luminance()
            corTexto.value=when{
                (luminessenciaBackgraud == 0.0f)-> textColorSquemas
                (luminessenciaBackgraud>0.0f&&luminessenciaBackgraud<0.1f) ->Color.White
                (luminessenciaBackgraud>=0.1f) ->Color.Black

                else -> Color.Unspecified
            }}
            else{
                corBackGraund.value=backgraudColorSquemas
                corTexto.value=textColorSquemas

            }
            acaoMudarCorScafollEBArraPermanente(corBackGraund.value,corTexto.value)

        }

}



class MedicoesItemsDeList(){
    fun gradCell(w: WindowSizeClass):Int =     if(w.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 1
    else if(w.windowWidthSizeClass== WindowWidthSizeClass.MEDIUM)
        if(w.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 2
        else 1
    else if (w.windowWidthSizeClass== WindowWidthSizeClass.EXPANDED)
        if (w.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 2
        else 3
    else   3
}

class MovimentoRetorno(){

    fun animacao(progreso: Float,
                 acaoDeVoutar: () -> Unit,
                 acaoMudarEscala:(x:Float,y:Float,ofsetx:Float,offsety:Float)->Unit,
                 acaoMudarCor: () -> Unit,
                 acaoReverterCorbackgrand:()->Unit){
        val x = (1f* progreso)
        val y = (1f * progreso)
        Log.d("progress animacao ","${progreso} ,x= $x,y=$y")
        acaoMudarEscala(x,y,x,y)
         if(progreso==1.0f) {
             acaoDeVoutar()
             return
         }
        if(x>=0.3f){
            acaoDeVoutar()
        }
        else if(x>=0.2f){
            acaoMudarCor()

        }
        else if(x<=0.1f){
            acaoReverterCorbackgrand()
            acaoMudarEscala(0f,0f,0f,0f)
        }

    }
}

class MedicoesPlyer(){
    fun larguraImagemPlyerCompoat(windowSizeClass: WindowSizeClass) =
         if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 0.7f
    else if (windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.MEDIUM) 0.6f
    else 0.4f
    fun larguraImagemPlyerEspandido(windowSizeClass: WindowSizeClass) =
         if(windowSizeClass.windowWidthSizeClass== WindowWidthSizeClass.COMPACT) 0.3f
    else 0.6f
 /**
  * valores usados no pLyer estendido
  * */
    fun spasamentoImagemTituloPlyerEstendido(windowSizeClass: WindowSizeClass)=
       if(windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 2.dp
    else 10.dp

    fun maxLineTextos(windowSizeClass: WindowSizeClass)=
        if(windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 1
       else 2
    fun funTSizeTitulo(windowSizeClass: WindowSizeClass)=
        if(windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 10.sp
        else 18.sp
    fun funTSizeSubtitulo()=14.sp
    fun tumblesize(windowSizeClass: WindowSizeClass) =
        if(windowSizeClass.windowHeightSizeClass== WindowHeightSizeClass.COMPACT) 6.dp
        else 10.dp



}

class MedicoesBotoesAuxiliare{
    fun fracaoDosBotoes(windowSizeClass: WindowSizeClass)=
         if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 0.35f
         else 0.3f
}

class MedicoesPaddingsListasEmPlylists{
    fun paddings(windowSizeClass: WindowSizeClass)=
        if(windowSizeClass.windowWidthSizeClass==WindowWidthSizeClass.COMPACT) 45.dp
        else 65.dp

}


@RequiresApi(Build.VERSION_CODES.Q)
suspend  fun getMetaData(uri: Uri, id: Long, context: Context, whidt:Int=100, height:Int=100):Bitmap?{
    Log.d("Metadata loaad tumb","id de media ${id} , uri ${uri}")
    try {
        val resolver = context.contentResolver
        val tumbmail=resolver.loadThumbnail(uri, Size(whidt,height),null)
        return tumbmail
    }catch (e:Exception){
        Log.d("Metadata loaad tumb","erro ao carregar tumbmail, ${e.message}")
        return null
    }

}