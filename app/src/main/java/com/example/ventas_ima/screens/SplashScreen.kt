package com.example.ventas_ima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val systemUiController = rememberSystemUiController()

    // 📌 Cambiar la barra de estado a blanco y los iconos a negro
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White, // Barra de estado blanca en el splash
            darkIcons = true // Iconos en negro
        )
    }

    // Controlar la animación de Lottie
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.example.ventas_ima.R.raw.splash_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1,
        speed = 1.3f,
        restartOnPlay = false
    )

    // Transición automática a la pantalla principal después de 3 segundos
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("provincias") {
            popUpTo("splash") { inclusive = true }
        }
    }

    // 🎨 Fondo blanco con barra de estado blanca
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 📌 Título agregado
            Text(
                text = "Sistema de Reporte de Ventas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // 🎥 Animación Lottie
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
