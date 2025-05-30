package com.vbteam.vibenote.ui.screens.notes

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vbteam.vibenote.R
import com.vbteam.vibenote.data.model.Emotion
import com.vbteam.vibenote.data.model.Note
import com.vbteam.vibenote.ui.components.AppButtonType
import com.vbteam.vibenote.ui.components.BaseAlertDialog
import com.vbteam.vibenote.ui.components.SearchTopBar
import com.vbteam.vibenote.ui.screens.notes.components.EmotionFilterBar
import com.vbteam.vibenote.ui.screens.notes.components.EmptyNotesPlaceholder
import com.vbteam.vibenote.ui.screens.notes.components.NoteItem
import com.vbteam.vibenote.ui.screens.notes.components.NotesDefaultTopBar
import com.vbteam.vibenote.ui.screens.notes.components.SelectionTopBar
import com.vbteam.vibenote.ui.theme.LocalAppDimens

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NotesScreenUI(
    navController: NavHostController,
    uiState: NotesUiState,
    onFilterSelected: (Emotion) -> Unit,
    onNoteClick: (Note) -> Unit,
    onNoteSelected: (String) -> Unit,
    onAddNoteClick: () -> Unit,
    onClearSelection: () -> Unit,
    onDeleteSelected: () -> Unit,
    onSearchClose: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    onSyncWithCloud: () -> Unit,
    searchHistory: List<Note>,
    clearSearchHistory: () -> Unit
) {
    BackHandler(enabled = uiState.selectedNotes.isNotEmpty() || uiState.isSearching) {
        when {
            uiState.selectedNotes.isNotEmpty() -> onClearSelection()
            uiState.isSearching -> onSearchClose()
        }
    }
    val haptic = LocalHapticFeedback.current

    var showDialogDeleteSelected by remember { mutableStateOf(false) }
    var showDialogСlearSearchHistory by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            Column {
                AnimatedContent(
                    targetState = uiState.selectedNotes.isNotEmpty() to uiState.isSearching,
                    transitionSpec = {
                        (fadeIn(tween(150))).togetherWith(
                            fadeOut(tween(150))
                        )
                    },
                    label = "TopBarTransition"
                ) { (isSelectionMode, isSearching) ->
                    when {
                        isSearching -> {
                            SearchTopBar(
                                searchQuery = uiState.searchQuery,
                                onSearchQueryChange = onSearchQueryChange,
                                onSearchClose = onSearchClose
                            )
                        }

                        isSelectionMode -> {
                            SelectionTopBar(
                                selectedCount = uiState.selectedNotes.size,
                                onDeleteClick = { showDialogDeleteSelected = true },
                                onClearSelection = onClearSelection
                            )
                        }

                        else -> {
                            NotesDefaultTopBar(
                                onProfileClick = { navController.navigate("profile") },
                                onSearchClick = onSearchClicked,
                                hasNotes = uiState.notes.isNotEmpty(),
                                onSyncClick = if (!uiState.isSyncing) onSyncWithCloud else null,
                                isSyncing = uiState.isSyncing
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = tween(durationMillis = 150)
                        )
                ) {
                    if (uiState.notes.isNotEmpty() && !uiState.isSearching) {
                        EmotionFilterBar(
                            selectedFilter = uiState.selectedFilter,
                            onFilterSelected = onFilterSelected,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 0.dp)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedContent(
                targetState = !uiState.isSearching,
                transitionSpec = {
                    (fadeIn(tween(150)) + scaleIn(tween(150))).togetherWith(
                        fadeOut(tween(150)) + scaleOut(tween(150))
                    )
                },
                label = "FABTransition",
            ) { shouldShowFAB ->
                if (shouldShowFAB) {
                    FloatingActionButton(
                        onClick = onAddNoteClick,
                        containerColor = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        modifier = Modifier.size(60.dp),
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pencil_white),
                            contentDescription = "Добавить запись",
                            modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = uiState.selectedFilter,
                transitionSpec = {
                    val slideSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 200)
                    val fadeSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 200)

                    if (targetState.ordinal > initialState.ordinal) {
                        (slideInHorizontally(animationSpec = slideSpec) { fullWidth -> fullWidth } + fadeIn(
                            animationSpec = fadeSpec
                        )).togetherWith(
                            slideOutHorizontally(animationSpec = slideSpec) { fullWidth -> -fullWidth } + fadeOut(
                                animationSpec = fadeSpec
                            ))
                    } else {
                        (slideInHorizontally(animationSpec = slideSpec) { fullWidth -> -fullWidth } + fadeIn(
                            animationSpec = fadeSpec
                        )).togetherWith(
                            slideOutHorizontally(animationSpec = slideSpec) { fullWidth -> fullWidth } + fadeOut(
                                animationSpec = fadeSpec
                            ))
                    }.using(SizeTransform(clip = false))
                },
                label = "TabSlideAnimation"
            ) { filter ->
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    val filteredNotes = when {
                        uiState.isSearching && uiState.searchQuery.isNotBlank() -> {
                            uiState.notes.filter {
                                it.content.contains(
                                    uiState.searchQuery,
                                    ignoreCase = true
                                )
                            }
                        }

                        uiState.isSearching && uiState.searchQuery.isBlank() -> {
                            searchHistory
                        }

                        else -> {
                            uiState.notes.filter { it.matchesFilter(uiState.selectedFilter) }
                        }
                    }

                    when {
                        /*
                        uiState.isLoading -> {
                            item {
                                AnimatedContent(
                                    targetState = uiState.isLoading,
                                    transitionSpec = {
                                        fadeIn(tween(150)).togetherWith(fadeOut(tween(150)))
                                    },
                                    label = "LoadingTransition"
                                ) { isLoading ->
                                    if (isLoading) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(top = 48.dp),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                            }
                        }
                        */

                        uiState.notes.isEmpty() -> {
                            item {
                                AnimatedContent(
                                    // Используем isEmpty как targetState
                                    targetState = uiState.notes.isEmpty(),
                                    transitionSpec = {
                                        fadeIn(tween(150)).togetherWith(fadeOut(tween(150)))
                                    },
                                    label = "EmptyNotesTransition"
                                ) { isEmpty ->
                                    if (isEmpty) {
                                        EmptyNotesPlaceholder()
                                    }
                                }
                            }
                        }

                        filteredNotes.isEmpty() -> {
                            item {
                                AnimatedContent(
                                    targetState = filteredNotes.isEmpty(),
                                    transitionSpec = {
                                        fadeIn(tween(150)).togetherWith(fadeOut(tween(150)))
                                    },
                                    label = "EmptyFilterTransition"
                                ) { isEmpty ->
                                    if (isEmpty) {
                                        Text(
                                            text = "Здесь пусто",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 20.dp),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        else -> {
                            if (uiState.isSearching && uiState.searchQuery.isBlank() && searchHistory.isNotEmpty()) {
                                item {
                                    AnimatedContent(
                                        targetState = uiState.isSearching && uiState.searchQuery.isBlank() && searchHistory.isNotEmpty(),
                                        transitionSpec = {
                                            fadeIn(tween(150)).togetherWith(fadeOut(tween(150)))
                                        },
                                        label = "SearchHistoryHeaderTransition"
                                    ) { shouldShowHeader ->
                                        if (shouldShowHeader) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        start = 12.dp,
                                                        end = 12.dp,
                                                        bottom = 12.dp
                                                    )
                                            ) {
                                                Text(
                                                    "История",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    "Очистить",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.clickable(onClick = {
                                                        showDialogСlearSearchHistory = true
                                                    })
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            items(filteredNotes) { note ->
                                val isSelected = uiState.selectedNotes.contains(note.id)
                                NoteItem(
                                    note = note,
                                    isSelected = isSelected,
                                    onClick = {
                                        if (uiState.selectedNotes.isNotEmpty()) {
                                            onNoteSelected(note.id)
                                        } else {
                                            onNoteClick(note)
                                        }
                                    },
                                    onLongClick = {
                                        if (!uiState.isSearching) {
                                            onNoteSelected(note.id)
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        }
                                    },
                                    modifier = Modifier.animateItem(
                                        fadeInSpec = null,
                                        fadeOutSpec = null,
                                        placementSpec = tween(150)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    if (showDialogDeleteSelected) {
        BaseAlertDialog(
            onDismiss = { showDialogDeleteSelected = false },
            onConfirmation = {
                onDeleteSelected()
                showDialogDeleteSelected = false
            },
            confirmButtonText = "Удалить",
            dismissButtonText = "Отмена",
            confirmButtonType = AppButtonType.ERROR,
            title = "Удалить записи",
            text = "Вы уверены, что хотите удалить выбранные записи? Это действие невозможно отменить.",
        )
    }

    if (showDialogСlearSearchHistory) {
        BaseAlertDialog(
            onDismiss = { showDialogСlearSearchHistory = false },
            onConfirmation = {
                clearSearchHistory()
                showDialogСlearSearchHistory = false
            },
            confirmButtonText = "Очистить",
            dismissButtonText = "Отмена",
            confirmButtonType = AppButtonType.ERROR,
            title = "Очистить историю поиска",
            text = "Очистить историю поиска?",
        )
    }
}

