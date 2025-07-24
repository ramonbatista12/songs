package com.songsSongs.songs.componentes

import android.content.Context
import android.graphics.drawable.Icon
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.AdChoicesView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.songsSongs.songs.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
/*
@Composable
fun Anuncio(modifier: Modifier=Modifier.height(100.dp),corDoTexto:Color=MaterialTheme.colorScheme.onBackground){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val estadoNativeAd=rememberAdLoader()
    var adLoader:AdLoader? =null
    var adRequest:AdRequest? =null
    val medidaDoIcone = with(LocalDensity.current){
        Size(60.dp.toPx(),60.dp.toPx())
    }
    val  larguraColunalogAdmob= with(LocalDensity.current){
        100.dp.toPx()
    }
    var adnative:NativeAdView? =null
    LaunchedEffect(estadoNativeAd.value) {
     scope.launch(Dispatchers.IO) {
         adRequest= AdRequest.Builder().build()

         adLoader=AdLoader.Builder(context,"ca-app-pub-3940256099942544/2247696110").forNativeAd { nativeAd ->
             estadoNativeAd.value = nativeAd
         }.withAdListener(object : AdListener() {
             override fun onAdFailedToLoad(p0: LoadAdError) {
                 super.onAdFailedToLoad(p0)
                 estadoNativeAd.value = null
             }

         })
         .withNativeAdOptions(NativeAdOptions.Builder()
                              .setRequestMultipleImages(false)
                              .setVideoOptions(
                                  VideoOptions.Builder().setStartMuted(true)
                                                        .build()
                                  )
                              .build())
         .build()
         adLoader?.loadAd(adRequest!!)
     }
    }
    DisposableEffect(Unit) {
        onDispose {
     if(estadoNativeAd.value!=null){
     estadoNativeAd.value?.destroy()

     }
     if(adnative!=null){

         adnative!!.destroy()
     }
     adnative=null
     estadoNativeAd.value=null
     adLoader=null
     adRequest=null
        }
    }

    if(estadoNativeAd.value!=null){
     AndroidView(modifier = modifier.wrapContentSize(),factory = {context->

          adnative=    NativeAdView(context).apply {
     val auxiliarCriarcomponente = AuxiliarCriarcomponente(context,corDoTexto)
     val auxiliarCriarLayouts = AuxiliArCriarLayouts(context)
     auxiliarCriarLayouts.adicionarALinhaPrincipal(auxiliarCriarcomponente.icone)
     auxiliarCriarLayouts.adicionarLayoutsALinhaPrincipal()
       //comfigurandoa linha do logo da admob
          auxiliarCriarLayouts.adicionarAColunaImagemLogAdmob(auxiliarCriarcomponente.imagemLogo)
          auxiliarCriarLayouts.adicionarAColunaImagemLogAdmob(auxiliarCriarcomponente.adChoicesView)
      //comfigurando o layout da coluna de informacoes

     auxiliarCriarLayouts.adicionarAColunaInformacoes(auxiliarCriarcomponente.titulo)
     auxiliarCriarLayouts.adicionarAColunaInformacoes(auxiliarCriarcomponente.body)
     auxiliarCriarLayouts.adicionarAColunaInformacoes(auxiliarCriarcomponente.mediaView)
     auxiliarCriarLayouts.adicionarAColunaInformacoes(auxiliarCriarcomponente.btn)


     auxiliarCriarLayouts.adicionarLayoutsaoNativeAdview(this)
     auxiliarCriarcomponente.adicionarTitulo(estadoNativeAd.value)
     auxiliarCriarcomponente.adicionarBody(estadoNativeAd.value)
     auxiliarCriarcomponente.adicionarIcone(estadoNativeAd.value,medidaDoIcone)
     auxiliarCriarcomponente.btn(estadoNativeAd.value)
     auxiliarCriarcomponente.adicionarcomteudoDeMedia(estadoNativeAd.value)
     auxiliarCriarcomponente.adicionaraoNAtiveView(this)
     this.setNativeAd(estadoNativeAd.value!!)



      }
    adnative!!
     })
    }


}

class AuxiliarCriarcomponente(val context: Context,val corDoTexto: Color){
    val body = TextView(context)
    val titulo = TextView(context)
    val imagemLogo= ImageView(context).apply {

        this.setImageDrawable(context.getDrawable(R.drawable.ad_badge))

    }
    val icone = ImageView(context)
    val btn = Button(context)
    val adChoicesView = AdChoicesView(context)
    val mediaView=MediaView(context)

    fun adicionarTitulo(ad: NativeAd?){
        ad?.headline.let { it->
            titulo.text =it
            val color:android.graphics.Color=android.graphics.Color()

            titulo.setTextColor(corDoTexto.toArgb())
        }
    }
    fun adicionarBody(ad: NativeAd?){
          ad?.body.let {it->
          body.text=it
          body.setTextColor(corDoTexto.toArgb())
          }
    }
     fun adicionarIcone(ad: NativeAd?,size:Size){
         ad?.icon.let {
             if(it!=null){
                 icone.setImageDrawable(it.drawable)
                 icone.layoutParams=LinearLayout.LayoutParams(size.height.toInt(),size.width.toInt())


             }
         }
     }

     fun btn(ad: NativeAd?){
         ad?.callToAction.let {
             btn.text =it
         }

     }

    fun adicionarcomteudoDeMedia(ad: NativeAd?){
        ad?.mediaContent?.let {
            mediaView.mediaContent=it


        }
    }

    fun adicionarLogo(ad: NativeAd?){
        ad?.adChoicesInfo.let {

        }
    }

        fun adicionaraoNAtiveView(nativeAdView: NativeAdView){
            nativeAdView.headlineView=titulo
            nativeAdView.bodyView=body
            nativeAdView.iconView=icone
            nativeAdView.callToActionView=btn
            nativeAdView.advertiserView=imagemLogo
            nativeAdView.adChoicesView=adChoicesView


        }


}


class AuxiliArCriarLayouts(context: Context){
    val linhaPrincipal =LinearLayout(context)
    val colunaInformacoes = LinearLayout(context)
    val colunaImagemLogAdmob = LinearLayout(context)
    init {
        linhaPrincipal.orientation =LinearLayout.HORIZONTAL
        linhaPrincipal.gravity =Gravity.CENTER_VERTICAL
        colunaInformacoes.orientation =LinearLayout.VERTICAL
        colunaImagemLogAdmob.orientation =LinearLayout.HORIZONTAL


    }
    fun adicionarLayoutsALinhaPrincipal(){
        colunaInformacoes.addView(colunaImagemLogAdmob)
        linhaPrincipal.addView(colunaInformacoes)
        //linhaPrincipal.addView()
    }
    fun adicionarALinhaPrincipal(view: android.view.View){
        linhaPrincipal.addView(view)
    }
    fun adicionarAColunaInformacoes(view: android.view.View){
        colunaInformacoes.addView(view)
    }
    fun adicionarAColunaImagemLogAdmob(view: android.view.View){
        colunaImagemLogAdmob.addView(view)
    }
    fun comfiguraLarguraColunaLogo(width:Float){
        val params =colunaImagemLogAdmob.layoutParams
        params.width=width.toInt()
        colunaImagemLogAdmob.layoutParams=params
    }
    fun adicionarLayoutsaoNativeAdview(view:NativeAdView){
        view.addView(linhaPrincipal)

    }
}
*/

@Composable
fun rememberAdLoader()= remember { mutableStateOf<NativeAd?>(null) }
/*
* nao sei o porque dessa bencao causar vasamento de memoria
* os vasamentos de main activit vem daqui
* nao so os vasamentos mas o uso esesivo de memoria tabem
* */
@Composable
fun Banner(modifier: Modifier=Modifier){





     AndroidView(modifier=Modifier.clip(RoundedCornerShape(15.dp)),
     factory = {context->

         AdView(context).apply {
             this.setAdSize(AdSize.BANNER)

             adUnitId=IdAdmob.BannerId.idTest
             loadAd(AdRequest.Builder().build())
         }
    })
}

sealed class IdAdmob(val id:String,val idTest:String="ca-app-pub-3940256099942544/6300978111"){
    object BannerId:IdAdmob( id="ca-app-pub-1950503385379483/4621080322")

}