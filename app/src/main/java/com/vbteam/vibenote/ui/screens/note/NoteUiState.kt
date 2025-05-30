package com.vbteam.vibenote.ui.screens.note

import com.vbteam.vibenote.data.model.Analysis
import com.vbteam.vibenote.data.model.Tag
import java.time.LocalDateTime

data class NoteUiState(
    // Note data
    val id: String? = null,
    val cloudId: String? = null,
    val content: String = "",
    val title: String = content.take(30),
    val tags: List<Tag> = emptyList(),
    val analysis: Analysis? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    // Sync state
    val syncState: SyncState = SyncState.NotSynced,
    val isAnalyzed: Boolean = false,

    // Loading states
    val loadingState: LoadingState = LoadingState.Idle,

    // UI Message state
    val uiMessage: UiMessage? = null
)

enum class LoadingState {
    Idle,
    Loading,
    CheckingSync,
    Analyzing
}

sealed class SyncState {
    data object NotSynced : SyncState()
    data object Synced : SyncState()
    data object SyncInProgress : SyncState()
}

sealed class UiMessage {
    data object NotFound : UiMessage()
    data object LoadError : UiMessage()
    data object SaveError : UiMessage()
    data object SaveSuccess : UiMessage()
    data object NotSynced : UiMessage()
    data object AnalysisError : UiMessage()
    data object AuthRequired : UiMessage()
    data object TooShortText : UiMessage()
    data object NoEdit : UiMessage()
    data object AnalysisSuccess : UiMessage()
}