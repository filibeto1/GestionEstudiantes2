package com.example.gestionestudiantes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestionestudiantes.data.local.database.AppDatabase
import com.example.gestionestudiantes.data.repository.EstudianteRepository
import com.example.gestionestudiantes.ui.screens.PantallaEstudiantes
import com.example.gestionestudiantes.ui.viewmodel.EstudianteViewModel
import com.example.gestionestudiantes.ui.viewmodel.EstudianteViewModelFactory

class MainActivity : ComponentActivity() {

    // Inicialización lazy de dependencias
    private val database by lazy {
        AppDatabase.getDatabase(this)
    }

    private val repository by lazy {
        EstudianteRepository(database.estudianteDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: EstudianteViewModel = viewModel(
                        factory = EstudianteViewModelFactory(repository)
                    )
                    PantallaEstudiantes(viewModel = viewModel)
                }
            }
        }
    }
}