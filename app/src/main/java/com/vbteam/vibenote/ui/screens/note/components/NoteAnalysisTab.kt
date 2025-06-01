package com.vbteam.vibenote.ui.screens.note.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vbteam.vibenote.R
import com.vbteam.vibenote.ui.components.AppButtonType
import com.vbteam.vibenote.ui.components.BaseAlertDialog
import com.vbteam.vibenote.ui.components.BaseButton
import com.vbteam.vibenote.ui.components.BaseCard
import com.vbteam.vibenote.ui.screens.note.LoadingState
import com.vbteam.vibenote.ui.screens.note.NoteUiState
import com.vbteam.vibenote.ui.screens.note.SyncState

@Composable
fun NoteAnalysisTab(uiState: NoteUiState, onAnalyzeClicked: () -> Unit) {
    val isSynced = uiState.syncState is SyncState.Synced
    val isAnalyzing = uiState.loadingState == LoadingState.Analyzing
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        BaseAlertDialog(
            onDismiss = { showConfirmDialog = false },
            onConfirmation = {
                showConfirmDialog = false
                onAnalyzeClicked()
            },
            confirmButtonText = "Анализ",
            confirmButtonType = AppButtonType.PRIMARY,
            dismissButtonText = "Отмена",
            text = "После анализа запись нельзя будет редактировать. Вы уверены, что хотите продолжить?",
            title = "Анализ записи"
        )
    }

    Log.d("NoteAnalysisTab", "State: isAnalyzed=${uiState.isAnalyzed}, tags=${uiState.tags}, analysis=${uiState.analysis}")

    // -- 1. Если запись еще не проанализирована --
    if (!uiState.isAnalyzed) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = if (isSynced) R.drawable.illustration_ok else R.drawable.illustration_cross),
                contentDescription = null,
                modifier = Modifier
                    .width(128.dp)
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (isSynced) "Текст готов к анализу" else "Текст не готов к анализу",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isSynced) 
                    "Нажмите на кнопку «Анализировать запись», чтобы получить рекомендации." 
                else 
                    "Сохраните запись в облако во вкладке «Запись», прежде чем его проанализировать.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            BaseButton(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.fillMaxWidth(),
                text = if (isAnalyzing) null else "Анализировать запись",
                type = AppButtonType.PRIMARY,
                enabled = isSynced && !isAnalyzing,
                content = if (isAnalyzing) {
                    { CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary) }
                } else null
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
    // -- 2. Если запись уже проанализирована --
    else {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            // 2.1. Эмоции
            BaseCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .border(
                        BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surface
                        ), 
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Эмоции", style = MaterialTheme.typography.titleSmall)

                    val sortedTags = uiState.tags.sortedByDescending { it.value }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in sortedTags.indices step 2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val tag1 = sortedTags[i]
                                val emotion1 = tag1.convertToEmotion()
                                EmotionTag(
                                    tag = tag1,
                                    emotion = emotion1,
                                    modifier = if (i + 1 < sortedTags.size) Modifier.weight(1f) else Modifier.fillMaxWidth()
                                )

                                if (i + 1 < sortedTags.size) {
                                    val tag2 = sortedTags[i + 1]
                                    val emotion2 = tag2.convertToEmotion()
                                    EmotionTag(
                                        tag = tag2,
                                        emotion = emotion2,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            // 2.2 Рекомендации
            BaseCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .border(
                        BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surface
                        ), 
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Рекомендации", style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = uiState.analysis?.result ?: "Реккомендаций нет.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }
        }
    }
}