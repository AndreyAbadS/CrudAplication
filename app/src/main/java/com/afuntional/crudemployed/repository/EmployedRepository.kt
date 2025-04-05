package com.afuntional.crudemployed.repository

import android.util.Log
import com.afuntional.crudemployed.api.EmployedApi.retrofitService
import com.afuntional.crudemployed.data.EmployedMapper
import com.afuntional.crudemployed.data.dtos.EmployedDTO
import com.afuntional.crudemployed.data.response.ApiResponseStatus
import com.afuntional.crudemployed.data.response.ApiResponseStatusDelete
import com.afuntional.crudemployed.data.response.ApiResponseStatusList
import com.afuntional.crudemployed.data.response.ApiResponseStatusPatch
import com.afuntional.crudemployed.utils.parseErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class EmployedRepository {
    suspend fun dowloadEmployeesList(): ApiResponseStatusList {
        return withContext(Dispatchers.IO) {
            try {
                val employedApiResponse = retrofitService.getAllEmployed()
                val employedDtoList = employedApiResponse.data
                val employedDTOMapper = EmployedMapper()
                ApiResponseStatusList.Success(
                    employedDTOMapper.fromEmployedDTOListToEmployedDomainList(
                        employedDtoList
                    )
                )
            } catch (e: Exception) {
                Log.d("hola", e.message.toString())
                ApiResponseStatusList.Error("Error en descarga $e")
            }
        }
    }

    suspend fun findEmployedById(id: Int): ApiResponseStatus {
        return withContext(Dispatchers.IO) {
            try {
                val employedApiResponse = retrofitService.getEmployedById(id)
                val employedDto = employedApiResponse.data
                val employedDTOMapper = EmployedMapper()
                ApiResponseStatus.Success(
                    employedDTOMapper.fromEmployedDTOToEmployedDomain(
                        employedDto
                    )
                )
            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                ApiResponseStatus.Error(errorMessage)
            } catch (e: Exception) {
                ApiResponseStatus.Error("Error en descarga")
            }
        }
    }

    suspend fun saveEmployed(employedDTO: EmployedDTO): ApiResponseStatus {
        return withContext(Dispatchers.IO) {
            try {
                val employedApiResponse = retrofitService.saveEmployed(employedDTO)
                val employedDto = employedApiResponse.data
                val employedDTOMapper = EmployedMapper()
                ApiResponseStatus.Success(
                    employedDTOMapper.fromEmployedDTOToEmployedDomain(
                        employedDto
                    )
                )
            } catch (e: Exception) {
                ApiResponseStatus.Error("Error subiendo empleado")
            }
        }
    }

    suspend fun updateEmployed(id: Int, employedDTO: EmployedDTO): ApiResponseStatus {
        return withContext(Dispatchers.IO) {
            try {
                val employedApiResponse = retrofitService.updateEmployed(id, employedDTO)
                val employedDto = employedApiResponse.data
                val employedDTOMapper = EmployedMapper()
                ApiResponseStatus.Success(
                    employedDTOMapper.fromEmployedDTOToEmployedDomain(
                        employedDto
                    )
                )
            } catch (e: Exception) {
                ApiResponseStatus.Error("Error en actualizar empleado")
            }
        }
    }

    suspend fun changeEmployeeStatus(id: Int): ApiResponseStatusPatch {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofitService.getChangeStatus(id)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val updateCount = responseBody?.data as? Int

                    if (updateCount == 1) {
                        ApiResponseStatusPatch.Success(responseBody.message)
                    } else {
                        ApiResponseStatusPatch.Error("No se actualizó ningún empleado.")
                    }
                } else {
                    val errorMessage = parseErrorMessage(response)
                    ApiResponseStatusPatch.Error(errorMessage)
                }

            } catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                ApiResponseStatusPatch.Error(errorMessage)
            } catch (e: Exception) {
                val errorMessage = parseErrorMessage(e)
                ApiResponseStatusPatch.Error(errorMessage)
            }
        }
    }

    suspend fun deleteEmployed(id: Int): ApiResponseStatusDelete {
        return withContext(Dispatchers.IO) {
            try {
                val response = retrofitService.deleteEmployed(id)
                if (response.isSuccessful) {
                    ApiResponseStatusDelete.Success("Borrado correctamente")
                } else {
                    val errorMessage = parseErrorMessage(response)
                    ApiResponseStatusDelete.Error(errorMessage)
                }

            }catch (e: HttpException) {
                val errorMessage = parseErrorMessage(e)
                ApiResponseStatusDelete.Error(errorMessage)
            } catch (e: Exception) {
                ApiResponseStatusDelete.Error("Error desconocido: ${e.message}")
            }
        }
    }


}