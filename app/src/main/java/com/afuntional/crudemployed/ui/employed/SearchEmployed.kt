package com.afuntional.crudemployed.ui.employed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afuntional.crudemployed.data.response.ApiResponseStatus
import com.afuntional.crudemployed.data.response.ApiResponseStatusDelete
import com.afuntional.crudemployed.data.response.ApiResponseStatusPatch
import com.afuntional.crudemployed.viewmodel.EmployedViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchEmployed(modifier: Modifier) {
    val viewModel: EmployedViewModel = viewModel()
    val employed by viewModel.employedDetails.collectAsState()
    val status by viewModel.status.collectAsState()
    val statusDelete by viewModel.statusDelete.collectAsState()
    var isSearching by remember { mutableStateOf(false) }
    var idEmployed by remember { mutableStateOf("") }
    var deleteMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    // Cuando el estado cambia a Success o Error, apagamos el loading manualmente
    LaunchedEffect(status) {
        if (status is ApiResponseStatus.Success || status is ApiResponseStatus.Error) {
            isSearching = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Buscar Empleado por ID", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = idEmployed,
            onValueChange = { idEmployed = it },
            label = { Text("ID del empleado") }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                val id = idEmployed.toIntOrNull()
                isSearching = true
                if (id != null) {
                    coroutineScope.launch {
                        viewModel.getEmployeeDetails(id)
                    }
                }

            }) {
                Text("Buscar")
            }
            Button(onClick = {
                val id = idEmployed.toIntOrNull()
                if (id != null) {
                    deleteMessage = null
                    coroutineScope.launch {
                        viewModel.deleteEmployee(id)
                    }
                } else {
                    deleteMessage = "ID inválido para eliminar"
                }
            }) {
                Text("Eliminar")
            }
        }
        when (statusDelete) {
            is ApiResponseStatusDelete.Loading -> {
                if (isSearching) {
                    CircularProgressIndicator()
                }
            }

            is ApiResponseStatusDelete.Success -> {
                Column {
                    Text(
                        text = (statusDelete as ApiResponseStatusDelete.Success).messageSucess,
                        color = Color.Green,
                        fontSize = 18.sp
                    )
                }

            }

            is ApiResponseStatusDelete.Error -> {
                Text(
                    text = (statusDelete as ApiResponseStatusDelete.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        when (status) {

            is ApiResponseStatus.Loading -> {
                if (isSearching) {
                    CircularProgressIndicator()
                }
            }

            is ApiResponseStatus.Success -> {
                employed?.let { employed ->
                    Column {
                        Text("ID: ${employed.idEmployed}", fontSize = 18.sp, color = Color.Black)
                        Text("Nombre: ${employed.nameEmployed}", fontSize = 18.sp, color = Color.Black)
                        Text(
                            "Apellido: ${employed.lastNameEmployed}",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text("Años: ${employed.ageEmployed}", fontSize = 16.sp, color = Color.Black)
                        Text("Rfc: ${employed.rfcEmployed}", fontSize = 16.sp, color = Color.Black)
                        if (employed.flagEmployed == 1) {
                            Text("Eliminado: Si", fontSize = 16.sp, color = Color.Black)
                        } else {
                            Text("Eliminado: No", fontSize = 16.sp, color = Color.Black)
                        }
                    }
                }
            }

            is ApiResponseStatus.Error -> {
                Column {
                    Text(
                        text = (status as ApiResponseStatus.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 18.sp
                    )
                }

            }
        }
    }


}
