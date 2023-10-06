package com.crystal.listeningcat

import android.app.Application
import com.crystal.listeningcat.message.MessageRepository

class LaunchApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        MessageRepository.init(this)
    }
}