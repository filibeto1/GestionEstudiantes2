package com.example.gestionestudiantes.data.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estudiantes")
data class Estudiante(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "nombre_completo")
    val nombre: String,

    @ColumnInfo(name = "matricula")
    val matricula: String,

    @ColumnInfo(name = "promedio")
    val promedio: Double,

    @ColumnInfo(name = "fecha_inscripcion")
    val fechaInscripcion: Long = System.currentTimeMillis()
)