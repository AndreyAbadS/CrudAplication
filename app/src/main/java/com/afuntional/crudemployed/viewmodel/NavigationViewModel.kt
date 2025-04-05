package com.afuntional.crudemployed.viewmodel

import androidx.lifecycle.ViewModel
import com.afuntional.crudemployed.navegation.Route

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class NavigationViewModel: ViewModel() {
    private val _navigation = MutableStateFlow<Route>(Route.EmployeeForm)
    val navigation: StateFlow<Route> = _navigation

    fun navigateTo(route: Route){
        _navigation.value = route
    }

    fun clearNavigation(){
        _navigation.value = Route.EmployeeList
    }
}