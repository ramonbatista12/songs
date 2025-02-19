package com.example.songs.componentes

import android.graphics.Bitmap

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
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
            if(palette.darkMutedSwatch!=null)
                int=palette.getDarkMutedColor(backgraudColor.value.toInt())
            else if(palette.mutedSwatch!=null)
                int=palette.getMutedColor(backgraudColor.value.toInt())
            else if(palette.dominantSwatch!=null)
                int=palette.getDominantColor(backgraudColor.value.toInt())
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

class MedicoesComtrolerPlyerEstendido(){
    fun tamanhoDoIcone(windowSizeClass: WindowSizeClass) = if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) 90.dp else 400.dp
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