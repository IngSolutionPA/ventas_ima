package com.example.ventas_ima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ventas_ima.viewmodel.DetalleViewModel
import com.example.ventas_ima.model.DetalleReporte
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporteDetallePantalla(
    provinciaSeleccionada: String,
    viewModel: DetalleViewModel = viewModel()
) {
    val provincias = listOf(
        "Bocas del Toro", "Coclé", "Colón", "Chiriquí",
        "Darién", "Herrera", "Los Santos", "Panamá", "Veraguas", "Panama Oeste"
    )

    var expandedProvincia by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Aplicar zona horaria de Panamá en el calendario
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    LaunchedEffect(provinciaSeleccionada) {
        viewModel.provincia = provinciaSeleccionada
        viewModel.cargarDatos()
    }

    val fondoVerde = Color(0xFF1B5C32)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = fondoVerde
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text(
                "Reportes de ventas",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Provincia
            ExposedDropdownMenuBox(
                expanded = expandedProvincia,
                onExpandedChange = { expandedProvincia = !expandedProvincia }
            ) {
                TextField(
                    value = viewModel.provincia,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccione una Provincia") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProvincia)
                    },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
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

            // Selector de Fecha con calendario
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
                modifier = Modifier.fillMaxWidth(),
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
                                    .plusMillis(5 * 60 * 60 * 1000) // UTC-5
                                    .atZone(ZoneId.of("UTC"))
                                    .toLocalDate()
                                    .toString()

                                viewModel.fecha = correctedDate
                                viewModel.cargarDatos()
                            }
                        }) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Reportes Generales de: ${viewModel.provincia}",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (viewModel.loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (viewModel.error != null) {
                Text("Error: ${viewModel.error}", color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.reportes) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.lugar, fontWeight = FontWeight.Medium)
                                Text("B/. ${item.total}", fontSize = 14.sp)
                            }
                        }
                    }
                }
                val totalArroz = viewModel.reportes.sumOf {
                    it.total_arroz.replace(",", "").toDoubleOrNull() ?: 0.0
                }
                val totalProductos = viewModel.reportes.sumOf {
                    it.total_productos.replace(",", "").toDoubleOrNull() ?: 0.0
                }
                val totalGeneral = viewModel.reportes.sumOf {
                    it.total.replace(",", "").toDoubleOrNull() ?: 0.0
                }
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Total en Arroz: B/. ${"%,.2f".format(totalArroz)}", fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Total en productos: B/. ${"%,.2f".format(totalProductos)}", fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Total generado: B/. ${"%,.2f".format(totalGeneral)}", fontWeight = FontWeight.Bold, color = Color.White)
                }


            }
        }
    }
}
