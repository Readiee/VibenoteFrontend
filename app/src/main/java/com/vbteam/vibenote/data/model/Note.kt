package com.vbteam.vibenote.data.model

import java.time.LocalDateTime
import java.util.UUID

data class Note(
    val id: String,
    val cloudId: String? = null,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val tags: List<Tag> = emptyList(),
    val analysis: Analysis? = null,
    var isSyncedWithCloud: Boolean = false
) {
    val isAnalyzed: Boolean
        get() = analysis != null

    fun copyWithUpdatedContent(newContent: String): Note {
        if (content == newContent) return this

        return copy(
            content = newContent,
            updatedAt = LocalDateTime.now(),
            isSyncedWithCloud = false
        )
    }

    fun convertTagToEmotion(tag: Tag): Emotion {
        return Emotion.entries.find { it.displayName == tag.name } ?: Emotion.DRAFT
    }

    fun getMajorEmotion(): Emotion {
        val majorTag = tags.maxByOrNull { it.value } ?: return Emotion.DRAFT
        return convertTagToEmotion(majorTag)
    }

    fun matchesFilter(filter: Emotion): Boolean {
        return when (filter) {
            Emotion.ALL -> true
            Emotion.DRAFT -> tags.isEmpty()
            else -> getMajorEmotion() == filter // самый сильный тэг
        }
    }

    companion object {
        fun create(content: String): Note {
            val now = LocalDateTime.now()
            return Note(
                id = UUID.randomUUID().toString(),
                cloudId = null,
                content = content,
                createdAt = now,
                updatedAt = now,
                isSyncedWithCloud = false
            )
        }
    }
}

data class Tag(
    val name: String,
    val value: Int
) {
    fun convertToEmotion(): Emotion {
        return Emotion.entries.find { it.displayName == name } ?: Emotion.DRAFT
    }
}

