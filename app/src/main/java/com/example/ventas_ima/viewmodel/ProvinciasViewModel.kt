package com.example.ventas_ima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.ventas_ima.model.ProvinciaReporte
import com.example.ventas_ima.data.RetrofitClient
import java.time.LocalDate

class ProvinciasViewModel : ViewModel() {
    var provincias by mutableStateOf<List<ProvinciaReporte>>(emptyList())
        private set

    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    var fecha by mutableStateOf(LocalDate.now().toString())
    var tipoReporte by mutableStateOf("ambos") // "tienda", "feria" o "ambos"

    init {
        cargarProvincias()
    }

    fun cargarProvincias() {
        viewModelScope.launch {
            loading = true
            try {
                provincias = RetrofitClient.apiService.obtenerTotalesProvinciasPorFecha(fecha, tipoReporte)
                error = null
            } catch (e: Exception) {
                error = e.message
            } finally {
                loading = false
            }
        }
    }

    fun cambiarTipoReporte(nuevoTipo: String) {
        tipoReporte = nuevoTipo
        cargarProvincias()
    }


}
