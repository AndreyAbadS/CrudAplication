package com.afuntional.crudemployed.api

import com.afuntional.crudemployed.data.dtos.EmployedDTO
import com.afuntional.crudemployed.data.response.EmployedApiResponse
import com.afuntional.crudemployed.data.response.EmployedApiResponseUpdate
import com.afuntional.crudemployed.data.response.EmployedListApiResponse
import com.afuntional.crudemployed.utils.Constans.Url_Static_Employed
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private val retrofit = Retrofit.Builder().baseUrl(Url_Static_Employed).addConverterFactory(MoshiConverterFactory.create()).build()

interface ApiService{
    @GET("employees")
    suspend fun getAllEmployed(): EmployedListApiResponse

    @GET("employedById/{id}")
    suspend fun getEmployedById(@Path("id") id: Int): EmployedApiResponse

    @POST("employed")
    suspend fun saveEmployed(@Body employed: EmployedDTO): EmployedApiResponse

    @PUT("employed/{id}")
    suspend fun updateEmployed(@Path("id") id: Int, @Body employed: EmployedDTO): EmployedApiResponse

    @PATCH("patch/{id}")
    suspend fun patchEmployed(@Path("id") id: Int, @Body employed: EmployedDTO): EmployedApiResponse

    @DELETE("delete/{id}")
    suspend fun deleteEmployed(@Path("id") id: Int): Response<Void>

    @PATCH("flag/{id}")
    suspend fun getChangeStatus(@Path("id") id: Int): Response<EmployedApiResponseUpdate>
}
object EmployedApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}