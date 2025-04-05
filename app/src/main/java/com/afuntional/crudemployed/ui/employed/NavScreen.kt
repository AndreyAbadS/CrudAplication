package com.afuntional.crudemployed.ui.employed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.afuntional.crudemployed.navegation.NavItem
import com.afuntional.crudemployed.navegation.NavigationHost
import com.afuntional.crudemployed.navegation.Route
import com.afuntional.crudemployed.viewmodel.NavigationViewModel

@Composable
fun NavScreen(navController: NavHostController){
    val viewModel: NavigationViewModel = viewModel()

    val navItemList = listOf(
        NavItem("Inicio", Icons.Default.Home),
        NavItem("Busqueda", Icons.Default.Search),
        NavItem("Agregar", Icons.Default.Person)
    )
    var selectedBottom by remember {
        mutableIntStateOf(2)
    }

    // Observamos el estado de navegación desde el ViewModel
    val currentRoute by viewModel.navigation.collectAsState()

    // Observamos y navegamos según el valor de currentRoute
    LaunchedEffect(currentRoute) {
        val currentDestination = navController.currentDestination?.route
        if (currentRoute.route != currentDestination) {
            navController.navigate(currentRoute.route) {
                launchSingleTop = true
                popUpTo(currentRoute.route) { inclusive = true }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(selected = selectedBottom == index,
                        onClick = {
                            selectedBottom = index

                            val selectedRoute = when (index) {
                                0 -> Route.EmployeeList
                                1 -> Route.EmployeeSearch
                                2 -> Route.EmployeeForm
                                else -> Route.EmployeeList
                            }
                            viewModel.navigateTo(selectedRoute)
                        }, icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon")
                        }, label = {
                            Text(text = navItem.label)
                        })
                }
            }
        }
    ) { innerPadding ->
        NavigationHost(innerPadding, navController)
    }
}