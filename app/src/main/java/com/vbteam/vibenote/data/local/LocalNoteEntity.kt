package com.vbteam.vibenote.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "notes")
data class LocalNoteEntity(
    @PrimaryKey val id: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)