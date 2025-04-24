package com.vbteam.vibenote.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vbteam.vibenote.R
import com.vbteam.vibenote.ui.components.AppButtonType
import com.vbteam.vibenote.ui.components.BaseButton
import com.vbteam.vibenote.ui.components.BaseInputField

@Composable
fun SignUpScreenUI(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 32.dp)
            .verticalScroll(state = rememberScrollState()),
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
            value = name,
            onValueChange = { name = it },
            hint = "Имя",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        BaseInputField(
            value = email,
            onValueChange = { email = it },
            hint = "Почта",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        BaseInputField(
            value = password,
            onValueChange = { password = it },
            hint = "Пароль",
            isPassword = true,
            showToggleVisibility = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        BaseInputField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            hint = "Подтвердите пароль",
            isPassword = true,
            showToggleVisibility = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        BaseButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            text = "Зарегистрироваться",
            type = AppButtonType.PRIMARY,
            enabled = true,
//            icon = Icons.AutoMirrored.Outlined.Login
        )

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
                    modifier = Modifier.clickable(onClick = { navController.popBackStack() })
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
            enabled = true,
        )
    }
}