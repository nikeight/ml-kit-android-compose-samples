package com.example.cameraview.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cameraview.composables.HomeScreen
import com.example.cameraview.composables.QRScannerScreen
import com.example.cameraview.composables.RegistrationScreen
import com.example.cameraview.ui.theme.CameraViewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CameraViewTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Destinations.HOME_SCREEN.name
                ) {
                    composable(Destinations.HOME_SCREEN.name) {
                        HomeScreen {
                            navController.navigate(Destinations.QR_SCAN_SCREEN.name)
                        }
                    }

                    composable(Destinations.QR_SCAN_SCREEN.name) {
                        QRScannerScreen(
                            onSuccessBarCodeAnalysis = { uniqueId ->
                                val id = uniqueId.toString()
                                navController.navigate("${Destinations.REGISTRATION_SCREEN.name}/${id}")
                            }, applicationContext = application
                        )
                    }

                    composable(
                        route = Destinations.REGISTRATION_SCREEN.name + "/{userId}",
                        arguments = listOf(
                            navArgument(name = "userId") {
                                type = NavType.StringType
                                defaultValue = "default_value"
                            }
                        )
                    ) {
                        RegistrationScreen(uniqueId = it.arguments?.getString("userId") ?: "")
                    }
                }
            }
        }
    }
}

enum class Destinations {
    HOME_SCREEN,
    QR_SCAN_SCREEN,
    REGISTRATION_SCREEN
}


