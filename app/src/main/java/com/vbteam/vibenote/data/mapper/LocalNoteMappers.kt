package com.vbteam.vibenote.data.mapper

import com.vbteam.vibenote.data.local.LocalNoteEntity
import com.vbteam.vibenote.data.model.Note

fun LocalNoteEntity.toDomain(): Note {
    return Note(
        id = id,
        cloudId = cloudId,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSyncedWithCloud = isSyncedWithCloud,
        analysis = analysis,
        tags = tags
    )
}

fun Note.toEntity(): LocalNoteEntity {
    return LocalNoteEntity(
        id = id,
        cloudId = cloudId,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSyncedWithCloud = isSyncedWithCloud,
        analysis = analysis,
        tags = tags
    )
}
