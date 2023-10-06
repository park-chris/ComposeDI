package com.crystal.listeningcat.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MessageDao {

    @Query("SELECT * FROM message")
    fun getMessages(): List<Message>

    @Query("SELECT * FROM message WHERE `index`=(:index)")
    fun getMessage(index: String): Message?

    @Update
    fun updateMessage(message: Message)

    @Insert
    fun addMessage(message: Message)

    @Delete
    fun deleteMessage(message: Message)
}