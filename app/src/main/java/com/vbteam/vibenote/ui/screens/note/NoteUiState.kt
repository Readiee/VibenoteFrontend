package com.vbteam.vibenote.ui.screens.note

import com.vbteam.vibenote.data.model.Emotion
import java.time.LocalDateTime

data class NoteUiState(
    val id: String? = null,
    val title: String? = null,
    val text: String = "",
    val emotion: Emotion = Emotion.DRAFT,
    val isSyncedWithCloud: Boolean = false, // пока false
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,

    val showAuthRequiredDialog: Boolean = false,

    val isUpdated: Boolean = false
)