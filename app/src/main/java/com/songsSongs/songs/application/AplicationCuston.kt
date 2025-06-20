package com.songsSongs.songs.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.songsSongs.songs.repositorio.RepositorioService




class AplicationCuston(): Application(){

   @SuppressLint("NewApi")
   @RequiresApi(Build.VERSION_CODES.Q)
   companion object conteiner {
        lateinit var context: Context
        val repositorio by lazy { RepositorioService(context = context) }
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    }

    override fun onCreate() {
        super.onCreate()
        context =applicationContext}
   override fun onTerminate() {
       super.onTerminate()
   }

}