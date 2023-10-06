package com.crystal.listeningcat.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Message(
    @PrimaryKey val index: String = UUID.randomUUID().toString(),
    var message: String = ""
) {
}
