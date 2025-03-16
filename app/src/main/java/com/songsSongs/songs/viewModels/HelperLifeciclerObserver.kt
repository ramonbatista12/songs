package com.songsSongs.songs.viewModels

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class HelperLifeciclerObserver(val acaoDeConectar: () -> Unit,val acaoDeDesconectar: () -> Unit,val acaoChecagemConecao: () -> Unit): DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner){
        acaoDeConectar()
    }
    override fun onPause(owner: LifecycleOwner) {
        acaoDeDesconectar()

    }
    override fun onStop(owner: LifecycleOwner) {}
    override fun onResume(owner: LifecycleOwner) {
       acaoChecagemConecao()
    }
}