package com.vbteam.vibenote.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vbteam.vibenote.data.model.Analysis
import com.vbteam.vibenote.data.model.Tag
import java.time.LocalDateTime

@Entity(tableName = "notes")
@TypeConverters(Converters::class)
data class LocalNoteEntity(
    @PrimaryKey val id: String,
    val cloudId: String? = null,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isSyncedWithCloud: Boolean = false,
    val analysis: Analysis? = null,
    val tags: List<Tag> = emptyList()
)