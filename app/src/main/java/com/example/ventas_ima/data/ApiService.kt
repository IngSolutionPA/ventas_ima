package com.example.ventas_ima.data

import com.example.ventas_ima.model.ProvinciaReporte
import com.example.ventas_ima.model.DetalleReporte
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("resumen_provincias.php")
    suspend fun obtenerTotalesProvincias(): List<ProvinciaReporte>

    @GET("resumen_provincia_fecha.php")
    suspend fun obtenerReporteProvinciaFecha(
        @Query("provincia") provincia: String,
        @Query("fecha") fecha: String,
        @Query("tipo") tipo: String
    ): List<DetalleReporte>

    @GET("resumen_provincias.php")
    suspend fun obtenerTotalesProvinciasPorFecha(
        @Query("fecha") fecha: String,
        @Query("tipo") tipo: String // ✅ Se agrega el nuevo parámetro
    ): List<ProvinciaReporte>


}
