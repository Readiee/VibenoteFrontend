package com.vbteam.vibenote.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vbteam.vibenote.ui.components.AppButtonType
import com.vbteam.vibenote.ui.components.BaseAlertDialog
import com.vbteam.vibenote.ui.components.BaseButton
import com.vbteam.vibenote.ui.components.BaseCard
import com.vbteam.vibenote.ui.components.BaseInputField
import com.vbteam.vibenote.ui.components.BaseTopBar
import com.vbteam.vibenote.ui.components.NullAlignTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenUI(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Диалог подтверждения выхода
    if (viewModel.showLogoutDialog) {
        BaseAlertDialog(
            onDismiss = { viewModel.dismissLogoutDialog() },
            onConfirmation = {
                viewModel.logout()
                navController.navigate("sign_in") {
                    popUpTo("profile") { inclusive = true }
                }
            },
            title = "Выход из аккаунта",
            text = "Вы действительно хотите выйти из аккаунта?",
            confirmButtonText = "Выйти",
            dismissButtonText = "Отмена",
            confirmButtonType = AppButtonType.ERROR
        )
    }

    Scaffold(
        topBar = {
            BaseTopBar(
                modifier = Modifier.shadow(
                    elevation = 1.dp,
                    spotColor = MaterialTheme.colorScheme.onSurface
                ),
                leftContent = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ChevronLeft,
                            contentDescription = "Назад",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                centerContent = {
                    Text(
                        text = "Профиль",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                rightContent = {
                    NullAlignTopBar()
                }
            )
        }
    ) { innerPadding ->
        // 1. если пользователь НЕ авторизован
        if (!uiState.isAuthenticated || uiState.currentUser == null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BaseCard(modifier = Modifier.offset(y = ((-40).dp))) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Профиль",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Вы не авторизованы",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Авторизуйтесь, чтобы получить больше возможностей приложения.",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        BaseButton(
                            text = "Войти",
                            onClick = { navController.navigate("sign_in") },
                            type = AppButtonType.SECONDARY,
                            icon = Icons.AutoMirrored.Rounded.Login,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        // 2. если пользователь авторизован
        else {
            uiState.currentUser?.let { user ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(state = rememberScrollState())
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    BaseCard(
                        modifier = Modifier
                            .padding(0.dp)
                            .border(
                                BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.surface,
                                ), shape = RoundedCornerShape(16.dp)
                            )
                            .clip(shape = RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "Профиль",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(48.dp),
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = user.username,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = user.login,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Форма изменения username
                    Text(
                        text = "Редактировать профиль",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    BaseInputField(
                        value = viewModel.name,
                        onValueChange = viewModel::onNameChanged,
                        hint = "Имя",
                        isError = viewModel.nameError != null,
                        errorMessage = viewModel.nameError,
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BaseButton(
                        onClick = { viewModel.changeUsername() },
                        modifier = Modifier.fillMaxWidth(),
                        text = if (uiState.isLoading) "Сохранение..." else "Сохранить",
                        type = AppButtonType.PRIMARY,
                        enabled = !uiState.isLoading && viewModel.isButtonEnabled
                    )

                    if (uiState.error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.surface,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 32.dp),
                    )

                    BaseButton(
                        onClick = { viewModel.showLogoutConfirmation() },
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.AutoMirrored.Filled.Logout,
                        text = if (uiState.isLoading) "Выход..." else "Выйти из аккаунта",
                        type = AppButtonType.SECONDARY,
                        enabled = !uiState.isLoading
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}