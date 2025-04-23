package com.vbteam.vibenote.data.model

import java.time.LocalDateTime

data class Note(
    val id: String,
    val title: String = "",
    val text: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val emotion: Emotion,
    val emotionName: String? = emotion.displayName,
    val emotionTagId: String? = null,
    val emotionValue: Int? = null,
    var isSyncedWithCloud: Boolean = false,
    val analysis: Analysis? = null
) {
    fun matchesFilter(filter: Emotion): Boolean {
        return when (filter) {
            Emotion.ALL -> true
            Emotion.DRAFT -> emotion == Emotion.DRAFT
            else -> emotion == filter
        }
    }
}

