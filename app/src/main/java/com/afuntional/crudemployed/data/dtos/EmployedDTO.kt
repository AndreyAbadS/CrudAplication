package com.afuntional.crudemployed.data.dtos

import com.squareup.moshi.Json

data class EmployedDTO(
    @Transient
    @field:Json(name = "idEmployed")
    val id: Int,
    @field:Json(name = "nameEmployed")
    val name: String,
    @field:Json(name = "lastNameEmployed")
    val lastName: String,
    @field:Json(name = "addressEmployed")
    val address: String,
    @field:Json(name = "ageEmployed")
    val age: Int,
    @field:Json(name = "flagEmployed")
    val flag: Int,
    @field:Json(name = "rfcEmployed")
    val rfc: String
)
