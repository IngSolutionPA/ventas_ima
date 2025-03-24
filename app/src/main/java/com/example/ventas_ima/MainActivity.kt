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
import com.example.ventas_ima.ui.theme.Ventas_imaTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Ventas_imaTheme {
                val systemUiController = rememberSystemUiController()
                val colorFondo = Color(0xFF1B5C32)

                SideEffect {
                    systemUiController.setStatusBarColor(color = colorFondo)
                }

                val navController: NavHostController = rememberNavController()

                NavHost(navController = navController, startDestination = "provincias") {
                    composable("provincias") {
                        ProvinciasPantalla { provinciaSeleccionada ->
                            navController.navigate("detalle/$provinciaSeleccionada")
                        }
                    }

                    composable("detalle/{provincia}") { backStackEntry ->
                        val provincia = backStackEntry.arguments?.getString("provincia") ?: ""
                        ReporteDetallePantalla(provinciaSeleccionada = provincia)
                    }
                }
            }
        }
    }
}
