package com.example.ventas_ima.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ventas_ima.viewmodel.DetalleViewModel
import java.time.Instant
import java.time.ZoneId
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporteDetallePantalla(
    provinciaSeleccionada: String,
    fechaSeleccionada: String,
    navController: NavController,
    viewModel: DetalleViewModel = viewModel()
) {
    val context = LocalContext.current
    val provincias = listOf(
        "Bocas del Toro", "Coclé", "Colón", "Chiriquí",
        "Darién", "Herrera", "Los Santos", "Panamá", "Veraguas", "Panamá Oeste"
    )
    fun obtenerIdProvincia(nombreProvincia: String): Int {
        val provinciasMap = mapOf(
            "Bocas del Toro" to 1,
            "Coclé" to 2,
            "Colón" to 3,
            "Chiriquí" to 4,
            "Darién" to 5,
            "Herrera" to 6,
            "Los Santos" to 7,
            "Panamá" to 8,
            "Veraguas" to 9,
            "Panamá Oeste" to 10
        )
        return provinciasMap[nombreProvincia] ?: 0
    }
    var selectedTipo by remember { mutableStateOf(viewModel.tipoReporte) }
    var expandedProvincia by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    val customGreen = Color(0xFFFFEE37)  // Verde oscuro para botones seleccionados
    //val customYellow = Color(0xFFFFEE37) // Amarillo para los totales

    LaunchedEffect(provinciaSeleccionada, fechaSeleccionada) {
        viewModel.provincia = provinciaSeleccionada
        viewModel.fecha = fechaSeleccionada
        viewModel.cargarDatos()
    }

    // ✅ Cálculo de totales
    val totalArroz = viewModel.reportes.sumOf { it.total_arroz.replace(",", "").toDoubleOrNull() ?: 0.0 }
    val totalProductos = viewModel.reportes.sumOf { it.total_productos.replace(",", "").toDoubleOrNull() ?: 0.0 }
    val totalGeneral = totalArroz + totalProductos

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ✅ **Encabezado**
            Surface(
                color = Color(0xFF1B5C32),
                modifier = Modifier.fillMaxWidth().height(80.dp),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.Assessment, contentDescription = "Reporte", tint = Color.White, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Detalle de Reportes", style = MaterialTheme.typography.headlineSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📌 **Selector de Provincia**
            ExposedDropdownMenuBox(
                expanded = expandedProvincia,
                onExpandedChange = { expandedProvincia = !expandedProvincia }
            ) {
                TextField(
                    value = viewModel.provincia,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccione una Provincia") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProvincia) },
                    modifier = Modifier.menuAnchor().fillMaxWidth().padding(horizontal = 16.dp),
                    colors = TextFieldDefaults.colors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledContainerColor = Color(0xFFF1F1F1),
                        focusedContainerColor = Color(0xFFF1F1F1),
                        unfocusedContainerColor = Color(0xFFF1F1F1)
                    )
                )
                ExposedDropdownMenu(
                    expanded = expandedProvincia,
                    onDismissRequest = { expandedProvincia = false }
                ) {
                    provincias.forEach { provincia ->
                        DropdownMenuItem(
                            text = { Text(provincia) },
                            onClick = {
                                viewModel.provincia = provincia
                                expandedProvincia = false
                                viewModel.cargarDatos()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 📆 **Selector de Fecha**
            TextField(
                value = viewModel.fecha,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha") },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Seleccionar Fecha")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color(0xFFF1F1F1),
                    focusedContainerColor = Color(0xFFF1F1F1),
                    unfocusedContainerColor = Color(0xFFF1F1F1)
                )
            )

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis?.let { millis ->
                                val correctedDate = Instant.ofEpochMilli(millis)
                                    .plusMillis(5 * 60 * 60 * 1000)
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                    .toString()

                                viewModel.fecha = correctedDate
                                viewModel.cargarDatos()
                            }
                        }) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
                    }
                ) { DatePicker(state = datePickerState) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ **Botones de selección de Tienda, Feria y Ambos estilizados**
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = { selectedTipo = "1"; viewModel.cambiarTipoReporte("1") },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedTipo == "1") customGreen else Color.LightGray)
                ) {
                    Icon(imageVector = Icons.Filled.Storefront, contentDescription = "Tienda", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tiendas")
                }

                Button(
                    onClick = { selectedTipo = "2"; viewModel.cambiarTipoReporte("2") },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedTipo == "2") customGreen else Color.LightGray)
                ) {
                    Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Feria", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ferias")
                }

                Button(
                    onClick = { selectedTipo = "ambos"; viewModel.cambiarTipoReporte("ambos") },
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedTipo == "ambos") customGreen else Color.LightGray)
                ) {
                    Icon(imageVector = Icons.Filled.List, contentDescription = "Ambos", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ambos")
                }
            }

            // 📌 **Lista de reportes con Card y botón de descarga**
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp).weight(1f)) {
                items(viewModel.reportes) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.lugar, fontWeight = FontWeight.Medium)
                                Text("B/. ${item.total}", fontSize = 14.sp)
                            }

                            // 📥 Botón de descarga
                            IconButton(
                                onClick = {
                                    val downloadUrl = "https://reportes.imakotlin.com/descargar_reporte.php" +
                                            "?id_reporte=${item.id_reporte}" +
                                            "&id_anio=${viewModel.fecha.substring(0, 4)}" +
                                            "&id_provincia=${obtenerIdProvincia(viewModel.provincia)}"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl))
                                    context.startActivity(intent)
                                }
                            ) {
                                Icon(Icons.Default.CloudDownload, contentDescription = "Descargar Reporte", tint = Color(0xFF1B5C32), modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }
            }

            // 📌 **Sección de Totales**
            Surface(
                color = Color(0xFF1B5C32),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total en Arroz: B/. ${"%,.2f".format(totalArroz)}", fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Total en Productos: B/. ${"%,.2f".format(totalProductos)}", fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Total General: B/. ${"%,.2f".format(totalGeneral)}", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
