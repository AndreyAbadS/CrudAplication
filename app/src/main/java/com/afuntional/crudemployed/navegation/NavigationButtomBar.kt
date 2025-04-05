package com.afuntional.crudemployed.navegation

import FormEmployed
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.afuntional.crudemployed.ui.employed.HomeScreenEmployed
import com.afuntional.crudemployed.ui.employed.SearchEmployed

@Composable
fun NavigationHost(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Route.EmployeeForm.route) {
        composable(Route.EmployeeList.route) {
            HomeScreenEmployed(modifier = Modifier.padding(innerPadding))
        }

        composable(Route.EmployeeSearch.route) {
            SearchEmployed(modifier = Modifier.padding(innerPadding))
        }

        composable(Route.EmployeeForm .route) {
            FormEmployed(modifier = Modifier.padding(innerPadding))
        }
    }
}