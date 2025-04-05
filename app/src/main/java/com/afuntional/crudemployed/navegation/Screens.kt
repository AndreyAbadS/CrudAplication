package com.afuntional.crudemployed.navegation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val route: String) {

    @Serializable
    object EmployeeList : Route("employee_list")

    @Serializable
    object EmployeeForm : Route("employee_form")

    @Serializable
    object EmployeeSearch : Route("employee_detail")

}