package com.example.gestionestudiantes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestionestudiantes.data.local.entity.Estudiante
import com.example.gestionestudiantes.data.repository.EstudianteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EstudianteViewModel(private val repository: EstudianteRepository) : ViewModel() {

    // ========== ESTADO OBSERVABLE ==========
    val todosLosEstudiantes: StateFlow<List<Estudiante>> =
        repository.todosLosEstudiantes
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // ========== OPERACIONES CRUD ==========
    fun agregarEstudiante(nombre: String, matricula: String, promedio: Double) {
        viewModelScope.launch {
            val nuevoEstudiante = Estudiante(
                nombre = nombre,
                matricula = matricula,
                promedio = promedio
            )
            repository.insertar(nuevoEstudiante)
        }
    }

    fun actualizarEstudiante(estudiante: Estudiante) {
        viewModelScope.launch {
            repository.actualizar(estudiante)
        }
    }

    fun eliminarEstudiante(estudiante: Estudiante) {
        viewModelScope.launch {
            repository.eliminar(estudiante)
        }
    }

    // ========== BÚSQUEDAS ==========
    fun buscarEstudiante(matricula: String, onResult: (Estudiante?) -> Unit) {
        viewModelScope.launch {
            val estudiante = repository.buscarPorMatricula(matricula)
            onResult(estudiante)
        }
    }

    // ========== CÁLCULOS ==========
    fun calcularEstadisticas(onResult: (Int, Double) -> Unit) {
        viewModelScope.launch {
            val total = repository.obtenerConteo()
            val promedioGeneral = repository.calcularPromedioGeneral()
            onResult(total, promedioGeneral)
        }
    }
}

// Fuera de la clase EstudianteViewModel, al final del archivo:
class EstudianteViewModelFactory(
    private val repository: EstudianteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstudianteViewModel::class.java)) {
            return EstudianteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}