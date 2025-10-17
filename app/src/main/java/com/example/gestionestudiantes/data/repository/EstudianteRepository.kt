package com.example.gestionestudiantes.data.repository

import com.example.gestionestudiantes.data.local.dao.EstudianteDao
import com.example.gestionestudiantes.data.local.entity.Estudiante
import kotlinx.coroutines.flow.Flow

class EstudianteRepository(private val estudianteDao: EstudianteDao) {

    // ========== OBSERVACIÓN DE DATOS ==========
    val todosLosEstudiantes: Flow<List<Estudiante>> =
        estudianteDao.obtenerTodosLosEstudiantes()

    fun obtenerDestacados(promedioMinimo: Double): Flow<List<Estudiante>> {
        return estudianteDao.obtenerEstudiantesDestacados(promedioMinimo)
    }

    // ========== OPERACIONES CRUD ==========
    suspend fun insertar(estudiante: Estudiante) {
        estudianteDao.insertarEstudiante(estudiante)
    }

    suspend fun insertarVarios(estudiantes: List<Estudiante>) {
        estudianteDao.insertarEstudiantes(estudiantes)
    }

    suspend fun actualizar(estudiante: Estudiante) {
        estudianteDao.actualizarEstudiante(estudiante)
    }

    suspend fun eliminar(estudiante: Estudiante) {
        estudianteDao.eliminarEstudiante(estudiante)
    }

    suspend fun eliminarTodos() {
        estudianteDao.eliminarTodos()
    }

    // ========== BÚSQUEDAS ESPECÍFICAS ==========
    suspend fun buscarPorMatricula(matricula: String): Estudiante? {
        return estudianteDao.buscarPorMatricula(matricula)
    }

    suspend fun obtenerConteo(): Int {
        return estudianteDao.contarEstudiantes()
    }

    suspend fun calcularPromedioGeneral(): Double {
        return estudianteDao.calcularPromedioGeneral() ?: 0.0
    }
}