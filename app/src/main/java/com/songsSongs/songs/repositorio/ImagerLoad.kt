package com.songsSongs.songs.repositorio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.collection.LruCache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ImagerLoad(private var c: Context?):InterfasseImagerLoader {
    private val cache:CacheImage? = CacheImage(c!!)
    override suspend fun getBitmap(uri: Uri): Bitmap?{
        val resposta = cache?.getBitmap(uri)
        when (resposta) {
            is BitmapResponce.Error -> return null
            is BitmapResponce.Success -> return resposta.bitmap
            else -> return null
        }

    }
     suspend fun getBitmap(uri: Uri,whidt:Int,height:Int): Bitmap?{
        val resposta = cache?.getBitmap(uri)
        when (resposta) {
            is BitmapResponce.Error -> return null
            is BitmapResponce.Success -> return resposta.bitmap
            else -> return null
        }

    }
    fun finalizar(){
        cache?.finalizar()
    }


}

class CacheImage(val c: Context) {
    private val tag = "CacheImageLoader"
    private var lruCache: LruCache<Uri, Bitmap>? = null
    private val mutex = Mutex()
    private val loadImageDisck = LoadImageDisck(c)

    init {
        val memoriaDoApp = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val memoriaDestinadaAoCache = memoriaDoApp / 20
        Log.d(tag, "memoria destinada ao cache $memoriaDestinadaAoCache memoria destinada ao app $memoriaDoApp")
        lruCache = object : LruCache<Uri, Bitmap>(memoriaDestinadaAoCache) {
            override fun sizeOf(key: Uri, value: Bitmap): Int {
                return (value.byteCount /1024).toInt()
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: Uri,
                oldValue: Bitmap,
                newValue: Bitmap?
            ) {
                oldValue.recycle()
                super.entryRemoved(evicted, key, oldValue, newValue)
            }

        }}
        suspend fun getBitmap(uri: Uri): BitmapResponce {
            Log.d(tag,"items no lrucache ${lruCache?.size()}")
            mutex.withLock {
                val responce = lruCache?.get(uri)
                if (responce == null) {
                    val bitmap = loadImageDisck.loadImage(uri, c)
                    when(bitmap){
                        is BitmapResponce.Error->{
                            return BitmapResponce.Error
                        }
                        is BitmapResponce.Success->{
                            lruCache?.put(uri, bitmap.bitmap)
                            return BitmapResponce.Success(bitmap.bitmap)
                        }
                    }




                }
                return BitmapResponce.Success(bitmap = responce!!)
            }

            return BitmapResponce.Error
        }
    suspend fun getBitmap(uri: Uri,whidt: Int,height: Int): BitmapResponce {
        Log.d(tag,"items no lrucache ${lruCache?.size()}")
        mutex.withLock {
            val responce = lruCache?.get(uri)
            if (responce == null) {
                val bitmap = loadImageDisck.loadImage(uri, c,whidt,height)
                when(bitmap){
                    is BitmapResponce.Error->{
                        return BitmapResponce.Error
                    }
                    is BitmapResponce.Success->{
                        lruCache?.put(uri, bitmap.bitmap)
                        return BitmapResponce.Success(bitmap.bitmap)
                    }
                }




            }
            return BitmapResponce.Success(bitmap = responce!!)
        }

        return BitmapResponce.Error
    }

        fun finalizar() {
            lruCache = null
            loadImageDisck.finalizar()
        }
    }

class LoadImageDisck(private var c:Context?){
    val contentResolver=c?.contentResolver
    suspend fun loadImage(uri: Uri,context: Context,whidt:Int=100,height:Int=100):BitmapResponce{
            Log.d("Metadata loaad tumb","id de media  , uri ${uri}")
            try {

                val tumbmail=contentResolver?.loadThumbnail(uri, Size(whidt,height),null)
                if(tumbmail!=null) return BitmapResponce.Success(tumbmail)
                return BitmapResponce.Error
            }catch (e:Exception){
                Log.d("Metadata loaad tumb","erro ao carregar tumbmail, ${e.message}")
                return BitmapResponce.Error
            }

        }
    fun finalizar(){
        c=null
    }

}

sealed class BitmapResponce(){
    data class Success(val bitmap: Bitmap):BitmapResponce()
    object Error:BitmapResponce()

}
