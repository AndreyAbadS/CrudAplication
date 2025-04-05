import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afuntional.crudemployed.data.dtos.EmployedDTO
import com.afuntional.crudemployed.data.response.ApiResponseStatus
import com.afuntional.crudemployed.data.response.ApiResponseStatusPatch
import com.afuntional.crudemployed.viewmodel.EmployedViewModel
import kotlinx.coroutines.launch

@Composable
fun FormEmployed(modifier: Modifier = Modifier) {
    val viewModel: EmployedViewModel = viewModel()
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var idUpdate by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var flag by remember { mutableStateOf("") }
    var rfc by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    // Estado local para mostrar el CircularProgressIndicator
    var isLoading by remember { mutableStateOf(false) }

    val status by viewModel.status.collectAsState()
    val statusUpdate by viewModel.statusUpdate.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Limpiar campos
    LaunchedEffect(status) {
        when (status) {
            is ApiResponseStatus.Success -> {
                name = ""
                lastName = ""
                address = ""
                age = ""
                flag = ""
                rfc = ""
                idUpdate = ""
                message = "Operación exitosa"
                isLoading = false
            }
            is ApiResponseStatus.Error -> {
                message = (status as ApiResponseStatus.Error).message
                isLoading = false
            }
            else -> {}
        }
    }
    LaunchedEffect(statusUpdate) {
        if (statusUpdate is ApiResponseStatusPatch.Success || statusUpdate is ApiResponseStatusPatch.Error) {
            isLoading = false
            message = when (statusUpdate) {
                is ApiResponseStatusPatch.Success -> (statusUpdate as ApiResponseStatusPatch.Success).messageSucess
                is ApiResponseStatusPatch.Error -> (statusUpdate as ApiResponseStatusPatch.Error).message
                else -> null
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Gestión de Empleados", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") })
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") })
        OutlinedTextField(
            value = age,
            onValueChange = { age = it.filter { char -> char.isDigit() } },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(value = rfc, onValueChange = { rfc = it }, label = { Text("RFC") })
        OutlinedTextField(
            value = idUpdate,
            onValueChange = { idUpdate = it.filter { char -> char.isDigit() } },
            label = { Text("ID para actualizar (Solo actualizar)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            keyboardController?.hide()
            val ageInt = age.toIntOrNull()
            if (ageInt != null) {
                val dto = EmployedDTO(
                    id = 0,
                    name = name,
                    lastName = lastName,
                    address = address,
                    age = ageInt,
                    flag = 0,
                    rfc = rfc
                )
                coroutineScope.launch {
                    isLoading = true
                    viewModel.saveEmployee(dto)
                }
            } else {
                message = "Por favor, ingrese números válidos en Edad y Eliminado"
            }
        }) {
            Text("Agregar Empleado")
        }

        Button(onClick = {
            keyboardController?.hide()
            val id = idUpdate.toIntOrNull()
            val ageInt = age.toIntOrNull()
            if (id != null && ageInt != null) {
                val dto = EmployedDTO(
                    id = id,
                    name = name,
                    lastName = lastName,
                    address = address,
                    age = ageInt,
                    flag = 0,
                    rfc = rfc
                )
                coroutineScope.launch {
                    isLoading = true
                    viewModel.updateEmployee(id, dto)
                }
            } else {
                message = "Por favor, ingrese números válidos en ID, Edad y Eliminado"
            }
        }) {
            Text("Actualizar Empleado")
        }

        Button(onClick = {
            keyboardController?.hide()
            val id = idUpdate.toIntOrNull()
            if (id != null) {
                coroutineScope.launch {
                    isLoading = true
                    viewModel.patchEmployee(id)
                }
            } else {
                message = "Ingrese un ID válido para cambiar el estado"
            }
        }) {
            Text("Cambiar estatus empleado")
        }

        message?.let {
            Text(text = it, color = Color.Green, fontSize = 16.sp)
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
