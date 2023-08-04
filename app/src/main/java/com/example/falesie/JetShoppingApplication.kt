package com.example.falesie

import android.app.Application
import android.util.Log

class JetShoppingApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        Graph.provideVie(this)
        Graph.provideFalesie(this)
        Log.d("APPLICAZIONE", "IN JETSHOPPINGAPP")


    }

}