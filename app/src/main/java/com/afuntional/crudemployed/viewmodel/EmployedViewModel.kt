package com.afuntional.crudemployed.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afuntional.crudemployed.data.dtos.EmployedDTO
import com.afuntional.crudemployed.data.model.Employed
import com.afuntional.crudemployed.data.response.ApiResponseStatus
import com.afuntional.crudemployed.data.response.ApiResponseStatusDelete
import com.afuntional.crudemployed.data.response.ApiResponseStatusList
import com.afuntional.crudemployed.data.response.ApiResponseStatusPatch
import com.afuntional.crudemployed.repository.EmployedRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployedViewModel:ViewModel() {
    private val employedRepository = EmployedRepository()

    // Estado para la lista de empleados
    private val _employedList = MutableStateFlow<List<Employed>>(emptyList())
    val employedList: StateFlow<List<Employed>> = _employedList

    // Estado para el empleado seleccionado
    private val _employedDetails = MutableStateFlow<Employed?>(null)
    val employedDetails: StateFlow<Employed?> = _employedDetails

    // Estados para respuestas de API
    private val _statusList = MutableStateFlow<ApiResponseStatusList>(ApiResponseStatusList.Loading())
    val statusList: StateFlow<ApiResponseStatusList> = _statusList

    private val _status = MutableStateFlow<ApiResponseStatus>(ApiResponseStatus.Loading())
    val status: StateFlow<ApiResponseStatus> = _status

    private val _statusUpdate = MutableStateFlow<ApiResponseStatusPatch>(ApiResponseStatusPatch.Loading())
    val statusUpdate: StateFlow<ApiResponseStatusPatch> = _statusUpdate

    private val _statusDelete = MutableStateFlow<ApiResponseStatusDelete>(ApiResponseStatusDelete.Loading())
    val statusDelete: StateFlow<ApiResponseStatusDelete> = _statusDelete

    init {
        downloadEmployees()
    }

    // Descargar lista de empleados
    private fun downloadEmployees() {
        viewModelScope.launch {
            _statusList.value = ApiResponseStatusList.Loading()
            val response = employedRepository.dowloadEmployeesList()
            handleResponseStatusList(response)
        }
    }

    // Obtener detalles de un empleado por ID
    fun getEmployeeDetails(id: Int) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            val response = employedRepository.findEmployedById(id)
            handleResponseStatus(response)
        }
    }

    // Guardar un nuevo empleado
    fun saveEmployee(employedDTO: EmployedDTO) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            val response = employedRepository.saveEmployed(employedDTO)
            handleResponseStatus(response)
            if (response is ApiResponseStatus.Success) {
                downloadEmployees() // Actualizar lista después de guardar solo si fue exitoso
            }
        }
    }

    // Actualizar un empleado existente
    fun updateEmployee(id: Int, employedDTO: EmployedDTO) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            val response = employedRepository.updateEmployed(id, employedDTO)
            handleResponseStatus(response)
            if (response is ApiResponseStatus.Success) {
                downloadEmployees()
            }
        }
    }

    // Actualiza el estatus de un empleado
    fun patchEmployee(id: Int) {
        viewModelScope.launch {
            _statusUpdate.value = ApiResponseStatusPatch.Loading()
            val response = employedRepository.changeEmployeeStatus(id)
            handleResponseStatusPatch(response)
            if (response is ApiResponseStatusPatch.Success) {
                downloadEmployees()
            }
        }
    }

    // Eliminar un empleado
    fun deleteEmployee(id: Int) {
        viewModelScope.launch {
            _statusDelete.value = ApiResponseStatusDelete.Loading()
            val response = employedRepository.deleteEmployed(id)
            handleResponseStatusDelete(response)
            if (response is ApiResponseStatusDelete.Success) {
                downloadEmployees()
            }
        }
    }

    // Manejo de respuestas para lista de empleados
    private fun handleResponseStatusList(response: ApiResponseStatusList) {
        if (response is ApiResponseStatusList.Success) {
            _employedList.value = response.employedList
        }
        _statusList.value = response
    }

    // Manejo de respuestas para un solo empleado
    private fun handleResponseStatus(response: ApiResponseStatus) {
        if (response is ApiResponseStatus.Success) {
            _employedDetails.value = response.employed
        }
        _status.value = response
    }

   // Manejo de patch
    private fun handleResponseStatusPatch(response: ApiResponseStatusPatch) {
        _statusUpdate.value = response
    }

    // Manejo de respuestas para eliminación de empleado
    private fun handleResponseStatusDelete(response: ApiResponseStatusDelete) {
        _statusDelete.value = response
    }
}