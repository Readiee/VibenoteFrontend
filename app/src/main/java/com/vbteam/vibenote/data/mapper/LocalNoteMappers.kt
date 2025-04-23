package com.vbteam.vibenote.data.mapper

import com.vbteam.vibenote.data.local.LocalNoteEntity
import com.vbteam.vibenote.data.model.Emotion
import com.vbteam.vibenote.data.model.Note

fun LocalNoteEntity.toDomain(): Note {
    return Note(
        id = this.id,
        title = this.content.take(60),
        text = this.content,
        emotion = Emotion.DRAFT,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        emotionName = null,
        emotionTagId = null,
        emotionValue = null,
        isSyncedWithCloud = false,
        analysis = null
    )
}

fun Note.toEntity(): LocalNoteEntity {
    return LocalNoteEntity(
        id = this.id,
        content = this.text,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
