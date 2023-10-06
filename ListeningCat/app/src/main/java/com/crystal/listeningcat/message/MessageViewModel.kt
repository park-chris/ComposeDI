package com.crystal.listeningcat.message

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageViewModel : ViewModel() {

    private val repository = MessageRepository.get()


    suspend fun getMessages(): List<Message> = withContext(Dispatchers.IO) {
        repository.getMessages()
    }

    suspend fun getMessage(index: String): Message? = withContext(Dispatchers.IO) {
        repository.getMessage(index)
    }

    fun addMessage(message: Message) {
        repository.addMessage(message)
    }

    fun updateMessage(message: Message) {
        repository.updateMessage(message)
    }

    fun deleteMessage(message: Message) {
        repository.deleteMessage(message)
    }


}