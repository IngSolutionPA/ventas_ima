package com.example.ventas_ima

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.SideEffect
import androidx.core.content.ContextCompat
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ventas_ima.screens.ProvinciasPantalla
import com.example.ventas_ima.screens.ReporteDetallePantalla
import com.example.ventas_ima.screens.SplashScreen
import com.example.ventas_ima.ui.theme.Ventas_imaTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // ✅ Pedir permiso para notificaciones en Android 13+ (API 33 o superior)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // ✅ Suscribirse al tema "reportes"
        FirebaseMessaging.getInstance().subscribeToTopic("reportes")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Suscripción a 'reportes' exitosa")
                } else {
                    Log.e("FCM", "Error al suscribirse a 'reportes'", task.exception)
                }
            }
        setContent {
            Ventas_imaTheme {
                val systemUiController = rememberSystemUiController()
                val navController: NavHostController = rememberNavController()

                NavHost(navController = navController, startDestination = "splash") {

                    // 🔥 Pantalla de Splash
                    composable("splash") {
                        SplashScreen(navController)
                    }

                    // 🟢 Pantalla de Provincias (Ahora pasa la fecha)
                    composable("provincias") {
                        SideEffect {
                            systemUiController.setStatusBarColor(
                                color = Color(0xFF1B5C32), // Verde
                                darkIcons = false // Iconos blancos
                            )
                        }
                        ProvinciasPantalla { provinciaSeleccionada, fechaSeleccionada ->
                            navController.navigate("detalle/$provinciaSeleccionada/$fechaSeleccionada")
                        }
                    }

                    // 🟢 Pantalla de Detalle (Recibe provincia y fecha)
                    composable("detalle/{provincia}/{fecha}") { backStackEntry ->
                        val provincia = backStackEntry.arguments?.getString("provincia") ?: ""
                        val fecha = backStackEntry.arguments?.getString("fecha") ?: ""

                        SideEffect {
                            systemUiController.setStatusBarColor(
                                color = Color(0xFF1B5C32), // Verde
                                darkIcons = false // Iconos blancos
                            )
                        }

                        ReporteDetallePantalla(
                            provinciaSeleccionada = provincia,
                            fechaSeleccionada = fecha,
                            navController = navController // ✅ Se pasa correctamente
                        )
                    }
                }
            }
        }
    }

    // 🔔 Solicitar permiso para notificaciones en Android 13+
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d("FCM", "Permiso de notificaciones concedido ✅")
            } else {
                Log.e("FCM", "Permiso de notificaciones denegado ❌")
            }
        }
}
