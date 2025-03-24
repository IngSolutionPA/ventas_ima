package com.example.ventas_ima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.ventas_ima.model.ProvinciaReporte
import com.example.ventas_ima.model.DetalleReporte
import com.example.ventas_ima.data.RetrofitClient
import java.time.LocalDate

class DetalleViewModel : ViewModel() {
    var reportes by mutableStateOf<List<DetalleReporte>>(emptyList())
    var provincia by mutableStateOf("Panamá")
    var fecha by mutableStateOf(LocalDate.now().toString())
    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    private val provinciasMap = mapOf(
        "Bocas del Toro" to "1",
        "Coclé" to "2",
        "Colón" to "3",
        "Chiriquí" to "4",
        "Darién" to "5",
        "Herrera" to "6",
        "Los Santos" to "7",
        "Panamá" to "8",
        "Veraguas" to "9",
        "Panamá Oeste" to "10",

    )


    fun cargarDatos() {
        viewModelScope.launch {
            loading = true
            try {
                val provinciaId = provinciasMap[provincia] ?: "1"
                reportes = RetrofitClient.apiService.obtenerReporteProvinciaFecha(provinciaId, fecha)
                println("Datos cargados: $fecha")
                error = null
            } catch (e: Exception) {
                error = e.message
                println("Error al cargar datos: ${e.message}")
            } finally {
                loading = false
            }
        }
    }


    init {
        cargarDatos()
    }
}
