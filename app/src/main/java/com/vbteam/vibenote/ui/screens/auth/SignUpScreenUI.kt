package com.vbteam.vibenote.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vbteam.vibenote.R
import com.vbteam.vibenote.ui.components.AppButtonType
import com.vbteam.vibenote.ui.components.BaseButton
import com.vbteam.vibenote.ui.components.BaseInputField

@Composable
fun SignUpScreenUI(navController: NavHostController) {
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Обработка успешной регистрации
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            navController.navigate("notes") {
                popUpTo("sign_up") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_ext),
                contentDescription = null,
                modifier = Modifier
                    .width(116.dp)
                    .aspectRatio(1f),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Регистрация",
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

            BaseInputField(
                value = viewModel.email,
                onValueChange = viewModel::onEmailChanged,
                hint = "Почта",
                isError = viewModel.emailError != null,
                errorMessage = viewModel.emailError,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            BaseInputField(
                value = viewModel.password,
                onValueChange = viewModel::onPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                hint = "Пароль",
                isPassword = true,
                showToggleVisibility = true,
                isError = viewModel.passwordError != null,
                errorMessage = viewModel.passwordError,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            BaseInputField(
                value = viewModel.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                hint = "Подтвердите пароль",
                isPassword = true,
                showToggleVisibility = true,
                isError = viewModel.confirmPasswordError != null,
                errorMessage = viewModel.confirmPasswordError,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            BaseButton(
                onClick = { viewModel.onRegisterClick() },
                modifier = Modifier.fillMaxWidth(),
                text = if (uiState.isLoading) "Регистрация..." else "Зарегистрироваться",
                type = AppButtonType.PRIMARY,
                enabled = !uiState.isLoading
            )

            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.error ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surface,
                    thickness = 1.dp,
                    modifier = Modifier.weight(1f)
                )
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        "Уже есть аккаунт?",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Войти",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(
                            onClick = { navController.popBackStack() },
                            enabled = !uiState.isLoading
                        )
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surface,
                    thickness = 1.dp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            BaseButton(
                onClick = {
                    navController.navigate("notes") {
                        popUpTo("notes") {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                text = "Продолжить без аккаунта",
                type = AppButtonType.SECONDARY,
                enabled = !uiState.isLoading
            )
        }
    }
}