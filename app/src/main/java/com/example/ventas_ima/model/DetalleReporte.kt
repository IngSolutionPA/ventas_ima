package com.example.ventas_ima.model

data class DetalleReporte(
    val id_reporte: Int,
    val lugar: String,
    val total_arroz: String,
    val total_productos: String,
    val total: String
)