package com.example.gestionestudiantes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gestionestudiantes.ui.viewmodel.EstudianteViewModel
import com.example.gestionestudiantes.data.local.entity.Estudiante

@OptIn(ExperimentalMaterial3Api::class)  // ← CAMBIAR A ExperimentalMaterial3Api
@Composable
fun PantallaEstudiantes(viewModel: EstudianteViewModel) {

    // Observar el estado de los estudiantes
    val estudiantes by viewModel.todosLosEstudiantes.collectAsState()

    // Estados locales para el formulario
    var nombre by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var promedio by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ========== ENCABEZADO ==========
        Text(
            text = "Gestión de Estudiantes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // ========== FORMULARIO DE REGISTRO ==========
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Registrar Nuevo Estudiante",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        mostrarError = false
                    },
                    label = { Text("Nombre completo") },
                    placeholder = { Text("Ej: Ana María López") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = matricula,
                    onValueChange = {
                        matricula = it
                        mostrarError = false
                    },
                    label = { Text("Matrícula") },
                    placeholder = { Text("Ej: 2024001") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = promedio,
                    onValueChange = {
                        promedio = it
                        mostrarError = false
                    },
                    label = { Text("Promedio") },
                    placeholder = { Text("Ej: 9.5") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (mostrarError) {
                    Text(
                        text = "Por favor completa todos los campos correctamente",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (nombre.isNotBlank() &&
                            matricula.isNotBlank() &&
                            promedio.isNotBlank()) {

                            val promedioValor = promedio.toDoubleOrNull()
                            if (promedioValor != null &&
                                promedioValor >= 0.0 &&
                                promedioValor <= 10.0) {

                                viewModel.agregarEstudiante(
                                    nombre = nombre.trim(),
                                    matricula = matricula.trim(),
                                    promedio = promedioValor
                                )

                                // Limpiar campos
                                nombre = ""
                                matricula = ""
                                promedio = ""
                                mostrarError = false
                            } else {
                                mostrarError = true
                            }
                        } else {
                            mostrarError = true
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar Estudiante")
                }
            }
        }

        // ========== LISTA DE ESTUDIANTES ==========
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lista de Estudiantes (${estudiantes.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (estudiantes.isNotEmpty()) {
                val promedioGeneral = estudiantes.map { it.promedio }.average()
                Text(
                    text = "Promedio: ${String.format("%.2f", promedioGeneral)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (estudiantes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay estudiantes registrados",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = estudiantes,
                    key = { estudiante -> estudiante.id }
                ) { estudiante ->
                    EstudianteItemTemporal(
                        estudiante = estudiante,
                        onEliminar = { viewModel.eliminarEstudiante(estudiante) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)  // ← CAMBIAR A ExperimentalMaterial3Api
@Composable
fun EstudianteItemTemporal(
    estudiante: Estudiante,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { /* Acción al hacer clic */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = estudiante.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Matrícula: ${estudiante.matricula}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Promedio: ${String.format("%.2f", estudiante.promedio)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (estudiante.promedio >= 9.0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }

            IconButton(
                onClick = onEliminar
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar estudiante",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}