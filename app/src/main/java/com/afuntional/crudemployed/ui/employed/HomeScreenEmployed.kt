package com.afuntional.crudemployed.ui.employed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afuntional.crudemployed.R
import com.afuntional.crudemployed.data.model.Employed
import com.afuntional.crudemployed.data.response.ApiResponseStatusList
import com.afuntional.crudemployed.viewmodel.EmployedViewModel

@Composable
fun HomeScreenEmployed(modifier: Modifier) {
    val viewModel: EmployedViewModel = viewModel()

    val employedList by viewModel.employedList.collectAsState()
    val statusList by viewModel.statusList.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.lista_de_empleados), fontSize = 26.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        when (statusList) {
            is ApiResponseStatusList.Loading -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }

            is ApiResponseStatusList.Success -> {
                LazyColumn {
                    items(employedList) { employed ->
                        EmployedItem(employed)
                    }
                }
            }

            is ApiResponseStatusList.Error ->{
                val errorMessage = (statusList as ApiResponseStatusList.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Error : $errorMessage", color = Color.Red)
                }
            }
        }
    }

}

@Composable
fun EmployedItem(employed: Employed) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        Text("Nombre: ${employed.nameEmployed}", fontSize = 18.sp, color = Color.White)
        Text("ID: ${employed.idEmployed}", fontSize = 18.sp, color = Color.White)
        Text("Apellido: ${employed.lastNameEmployed}", fontSize = 16.sp, color = Color.LightGray)
        Text("Rfc: ${employed.rfcEmployed}", fontSize = 16.sp, color = Color.LightGray)
        if (employed.flagEmployed == 1) {
            Text("Eliminado: Si", fontSize = 16.sp, color = Color.LightGray)
        }else{
            Text("Eliminado: No", fontSize = 16.sp, color = Color.LightGray)
    }


}
}
