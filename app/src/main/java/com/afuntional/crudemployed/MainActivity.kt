package com.afuntional.crudemployed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.afuntional.crudemployed.ui.employed.NavScreen
import com.afuntional.crudemployed.ui.theme.CRUDEmployedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CRUDEmployedTheme {
                val navController = rememberNavController()
                NavScreen(navController = navController)
            }
        }
    }
}
