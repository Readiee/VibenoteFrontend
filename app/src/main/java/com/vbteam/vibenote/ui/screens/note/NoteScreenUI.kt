package com.vbteam.vibenote.ui.screens.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vbteam.vibenote.ui.components.BaseSheetButton
import com.vbteam.vibenote.ui.components.BaseTopBar
import com.vbteam.vibenote.ui.components.DialogAuthRequirement
import com.vbteam.vibenote.ui.components.FancyTabs
import com.vbteam.vibenote.ui.components.NullAlignTopBar
import com.vbteam.vibenote.ui.screens.note.components.DialogSoonFuns
import com.vbteam.vibenote.ui.screens.note.components.NoteAnalysisTab
import com.vbteam.vibenote.ui.screens.note.components.NoteTextTab
import com.vbteam.vibenote.ui.theme.LocalAppDimens
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavHostController,
    uiState: NoteUiState,
    onBack: () -> Unit,
    onSaveButtonClicked: () -> Unit,
    onNoteChanged: (String) -> Unit,
    onDismissAuthDialog: () -> Unit,
    onConfirmationAuthDialog: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Запись", "Анализ")

    BackHandler {
        onBack()
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDialogSoonFuns by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
//                    .shadow(elevation = 1.dp, spotColor = MaterialTheme.colorScheme.onSurface)
                    .padding(0.dp)
            ) {
                BaseTopBar(
                    leftContent = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.Outlined.ChevronLeft,
                                contentDescription = "Назад",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                            )
                        }
                    }, centerContent = {
                        Text(
//                        text = "Запись",
                            text = uiState.title ?: "Новая запись",
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
                            IconButton(onClick = { showBottomSheet = true }) {
                                Icon(
                                    imageVector = Icons.Outlined.AddCircleOutline,
                                    contentDescription = "Добавить вложение",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(LocalAppDimens.current.iconSizeMedium)
                                )
                            }
                            Spacer(modifier = Modifier.width(32.dp))

                            Column {
                                Text(
                                    text = if (uiState.isSyncedWithCloud) "Сохранено в облако" else "Черновик",
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
                        FloatingActionButton(
                            onClick = onSaveButtonClicked,
                            containerColor = MaterialTheme.colorScheme.primary,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Outlined.CloudUpload, "Save on cloud")
                        }
                    }
                )

                1 -> null
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding() + 4.dp,
                    bottom = padding.calculateBottomPadding() - 20.dp,
                )
        ) {
//            TabSwitcher(selectedTabIndex = selectedTab) { tabIndex ->
//                when (tabIndex) {
//                    0 -> NoteTextTab(uiState = uiState, onNoteChanged = onNoteChanged)
//                    1 -> NoteAnalysisTab(uiState = uiState)
//                }
//            }
            when (selectedTab) {
                0 -> NoteTextTab(uiState = uiState, onNoteChanged = onNoteChanged)
                1 -> NoteAnalysisTab(uiState = uiState)
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

            if (uiState.showAuthRequiredDialog) {
                DialogAuthRequirement(
                    onDismiss = onDismissAuthDialog, onConfirmation = onConfirmationAuthDialog
                )
            }
        }
    }
}
