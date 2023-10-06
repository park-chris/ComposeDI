package com.crystal.listeningcat.message

import android.content.Context
import androidx.room.Room

import java.util.concurrent.Executors

class MessageRepository(context: Context) {
    companion object {
        private var INSTANCE: MessageRepository? = null
        private const val DATABASE_NAME = "message_database"

        fun init(context: Context) {
            if (INSTANCE == null)
                INSTANCE = MessageRepository(context)

        }

        fun get(): MessageRepository {
            return INSTANCE ?: throw IllegalStateException("MessageRepository INSTANCE get failed")
        }

    }

    private var database : MessageDatabase = Room.databaseBuilder(
        context.applicationContext,
        MessageDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val dao = database.messageDao()
    private val executor = Executors.newSingleThreadExecutor()



    fun getMessages(): List<Message> = dao.getMessages()
    fun getMessage(index: String): Message? = dao.getMessage(index)


    fun updateMessage(message: Message) {
      executor.execute {
          dao.updateMessage(message)
      }
    }

    fun addMessage(message: Message) {
        executor.execute {
            dao.addMessage(message)
        }
    }

    fun deleteMessage(message: Message) {
        executor.execute {
            dao.deleteMessage(message)
        }
    }
}