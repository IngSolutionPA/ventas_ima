package com.example.ventas_ima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}
