package com.example.gestionestudiantes.data.local.dao

import androidx.room.*
import com.example.gestionestudiantes.data.local.entity.Estudiante
import kotlinx.coroutines.flow.Flow

@Dao
interface EstudianteDao {

    // ========== OPERACIONES DE INSERCIÓN ==========
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEstudiante(estudiante: Estudiante)

    @Insert
    suspend fun insertarEstudiantes(estudiantes: List<Estudiante>)

    // ========== OPERACIONES DE ACTUALIZACIÓN ==========
    @Update
    suspend fun actualizarEstudiante(estudiante: Estudiante)

    // ========== OPERACIONES DE ELIMINACIÓN ==========
    @Delete
    suspend fun eliminarEstudiante(estudiante: Estudiante)

    @Query("DELETE FROM estudiantes")
    suspend fun eliminarTodos()

    // ========== OPERACIONES DE CONSULTA ==========
    @Query("SELECT * FROM estudiantes ORDER BY promedio DESC")
    fun obtenerTodosLosEstudiantes(): Flow<List<Estudiante>>

    @Query("SELECT * FROM estudiantes WHERE matricula = :matricula")
    suspend fun buscarPorMatricula(matricula: String): Estudiante?

    @Query("SELECT * FROM estudiantes WHERE promedio >= :promedioMinimo")
    fun obtenerEstudiantesDestacados(promedioMinimo: Double): Flow<List<Estudiante>>

    @Query("SELECT COUNT(*) FROM estudiantes")
    suspend fun contarEstudiantes(): Int

    @Query("SELECT AVG(promedio) FROM estudiantes")
    suspend fun calcularPromedioGeneral(): Double?
}