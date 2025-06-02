package com.vbteam.vibenote.ui.screens.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.rounded.MicNone
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vbteam.vibenote.ui.components.BaseSheetButton
import com.vbteam.vibenote.ui.components.BaseTopBar
import com.vbteam.vibenote.ui.components.CustomSnackbarHost
import com.vbteam.vibenote.ui.components.DialogAuthRequirement
import com.vbteam.vibenote.ui.components.FancyTabs
import com.vbteam.vibenote.ui.components.NullAlignTopBar
import com.vbteam.vibenote.ui.components.SnackbarType
import com.vbteam.vibenote.ui.components.showCustomSnackbar
import com.vbteam.vibenote.ui.screens.note.components.DialogSoonFuns
import com.vbteam.vibenote.ui.screens.note.components.NoteAnalysisTab
import com.vbteam.vibenote.ui.screens.note.components.NoteTextTab
import com.vbteam.vibenote.ui.theme.LocalAppDimens
import java.time.format.DateTimeFormatter
import androidx.compose.material3.CircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenUI(
    navController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Запись", "Анализ")

    LaunchedEffect(uiState.uiMessage) {
        uiState.uiMessage?.let { message ->
            when (message) {
                is UiMessage.TooShortText -> snackbarHostState.showCustomSnackbar(
                    title = "Слишком короткий текст",
                    message = "Для сохранения текст должен содержать минимум 50 символов",
                    type = SnackbarType.WARNING
                )

                is UiMessage.SaveError -> snackbarHostState.showCustomSnackbar(
                    title = "Не удалось сохранить",
                    message = "Проверьте интернет-соединение или повторите попытку позже",
                    type = SnackbarType.ERROR
                )

                is UiMessage.AnalysisError -> snackbarHostState.showCustomSnackbar(
                    title = "Не удалось проанализировать",
                    message = "Проверьте интернет-соединение или повторите попытку позже",
                    type = SnackbarType.ERROR
                )

                is UiMessage.NoEdit -> snackbarHostState.showCustomSnackbar(
                    title = "Редактирование невозможно",
                    message = "Эта запись уже проанализрована и не может быть изменена",
                    type = SnackbarType.WARNING
                )

                is UiMessage.SaveSuccess -> snackbarHostState.showCustomSnackbar(
                    title = "Сохранено в облако",
                    message = "Заметка успешно сохранена",
                    type = SnackbarType.SUCCESS
                )

                else -> { /* Другие типы сообщений обрабатываются отдельно */
                }
            }

            // Очищаем сообщение после показа, кроме AuthRequired, которое обрабатывается отдельно
            if (message !is UiMessage.AuthRequired) {
                viewModel.clearMessage()
            }
        }
    }

    BackHandler {
//        if (uiState.isSavingToCloud) {
//            return@BackHandler
//        }

        if (uiState.hasLocalChanges) {
            viewModel.saveNote(saveToCloud = false)
        }
        navController.navigateUp()
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDialogSoonFuns by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { CustomSnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
//                    .shadow(elevation = 1.dp, spotColor = MaterialTheme.colorScheme.onSurface)
                    .padding(0.dp)
            ) {
                BaseTopBar(
                    leftContent = {
                        IconButton(onClick = {
                            viewModel.saveNote(saveToCloud = false)
                            navController.navigateUp()
                        }) {
                            Icon(
                                Icons.Outlined.ChevronLeft,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                            )
                        }
                    }, centerContent = {
                        Text(
//                        text = "Запись",
                            text = if (uiState.id == null) "Новая запись" else uiState.title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    rightContent = { NullAlignTopBar() }
                )
                FancyTabs(
                    tabs = tabTitles,
                    selectedTabIndex = selectedTab,
                    onTabSelected = { selectedTab = it })
            }
        },
        bottomBar = {
//            AnimatedVisibility(
//                visible = selectedTab == 0,
//                enter = fadeIn(animationSpec = tween(durationMillis = 0)),
//                exit = fadeOut(animationSpec = tween(durationMillis = 0))
//            )
            when (selectedTab) {
                0 -> BottomAppBar(
                    modifier = Modifier
                        .shadow(
                            elevation = 1.dp, spotColor = MaterialTheme.colorScheme.onSurface
                        )
                        .border(
                            BorderStroke(
                                width = 1.dp, color = MaterialTheme.colorScheme.surface
                            )
                        ),
                    containerColor = MaterialTheme.colorScheme.background,
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            IconButton(onClick = {
                                if (!uiState.isAnalyzed) showBottomSheet = true
                                else viewModel.showMessage(UiMessage.NoEdit)
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.AddCircleOutline,
                                    contentDescription = "Добавить вложение",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .size(LocalAppDimens.current.iconSizeMedium)
                                        .alpha(if (uiState.isAnalyzed) 0.38f else 1f)
                                )
                            }
                            Spacer(modifier = Modifier.width(32.dp))

                            Column {
                                Text(
                                    text = when (uiState.syncState) {
                                        is SyncState.Synced -> "Сохранено в облако"
                                        is SyncState.SyncInProgress -> "Синхронизация..."
                                        is SyncState.NotSynced -> "Черновик"
                                        is SyncState.UnsyncedChanges -> "Не синхронизировано"
                                        is SyncState.Analyzed -> "Пронанализировано"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (uiState.updatedAt != null) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text =
                                            "Послед. изм. " + uiState.updatedAt.format(
                                                DateTimeFormatter.ofPattern("dd MMM, HH:mm")
                                            ),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        val interactionSource = remember { MutableInteractionSource() }
                        val isSynced = uiState.syncState is SyncState.Synced
                        val isSavingToCloud = uiState.isSavingToCloud
                        val isAnalyzed = uiState.syncState is SyncState.Analyzed
                        val isButtonDisabled = isSynced || isSavingToCloud || isAnalyzed

                        LaunchedEffect(isButtonDisabled) {
                            if (isButtonDisabled) {
                                interactionSource.emit(
                                    PressInteraction.Cancel(PressInteraction.Press(Offset.Zero))
                                )
                            }
                        }

                        FloatingActionButton(
                            onClick = {
                                if (!isButtonDisabled) {
                                    if (uiState.content.isBlank() || uiState.content.length < 50)
                                        viewModel.showMessage(UiMessage.TooShortText)
                                    else viewModel.saveNote(saveToCloud = true)
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            elevation = if (!isButtonDisabled)
                                FloatingActionButtonDefaults.bottomAppBarFabElevation()
                            else FloatingActionButtonDefaults.bottomAppBarFabElevation(0.dp),
                            shape = CircleShape,
                            modifier = Modifier.alpha(if (!isButtonDisabled) 1f else 0.38f),
                            interactionSource = interactionSource
                        ) {
                            if (uiState.isSavingToCloud) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.Outlined.CloudUpload,
                                    contentDescription = if (!isSynced)
                                        "Save on cloud"
                                    else "Already saved on cloud",
                                )
                            }
                        }
                    }
                )

                1 -> null
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            /*
            TabSwitcher(selectedTabIndex = selectedTab) { tabIndex ->
                when (tabIndex) {
                    0 -> NoteTextTab(uiState = uiState, onNoteChanged = onNoteChanged)
                    1 -> NoteAnalysisTab(uiState = uiState)
                }
            }
            */
            when (selectedTab) {
                0 -> NoteTextTab(
                    uiState = uiState,
                    onNoteChanged = { viewModel.updateContent(it) },
                    onTryEditNote = { viewModel.showMessage(UiMessage.NoEdit) })

                1 -> NoteAnalysisTab(
                    uiState = uiState,
                    onAnalyzeClicked = { viewModel.analyzeNote() }
                )
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    containerColor = MaterialTheme.colorScheme.background,
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                ) {
                    BaseSheetButton(
                        icon = Icons.Rounded.MicNone,
                        text = "Записать голос",
                        labelText = "Скоро",
                        onClick = {
                            showDialogSoonFuns = true
                            showBottomSheet = false
                        })
                    BaseSheetButton(
                        icon = Icons.Outlined.PhotoCamera,
                        text = "Сделать селфи",
                        labelText = "Скоро",
                        onClick = {
                            showDialogSoonFuns = true
                            showBottomSheet = false
                        })
                    BaseSheetButton(
                        icon = Icons.Outlined.Image,
                        text = "Загрузить селфи",
                        labelText = "Скоро",
                        onClick = {
                            showDialogSoonFuns = true
                            showBottomSheet = false
                        })
                    Spacer(Modifier.height(20.dp))
                }
            }

            if (showDialogSoonFuns) {
                DialogSoonFuns(onDismiss = {
                    showDialogSoonFuns = false
                })
            }

            if (uiState.uiMessage == UiMessage.AuthRequired) {
                DialogAuthRequirement(
                    onDismiss = { viewModel.clearMessage() },
                    onConfirmation = {
                        viewModel.clearMessage()
                        navController.navigate("sign_in")
                        viewModel.saveNote(saveToCloud = false)
                    }
                )
            }
        }
    }
}
