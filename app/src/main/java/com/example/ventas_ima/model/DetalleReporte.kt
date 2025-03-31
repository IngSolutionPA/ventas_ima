package com.example.ventas_ima.model
import com.google.gson.annotations.SerializedName

data class DetalleReporte(
    val id_reporte: Int,
    val lugar: String,
    val total_arroz: String,
    val total_productos: String,
    val total: String,
    @SerializedName("ruta_archivo") val ruta_archivo: String?
)