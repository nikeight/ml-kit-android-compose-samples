package com.example.geminichatapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.geminichatapp.features.channel.ChannelListScreen
import com.example.geminichatapp.features.login.LoginScreen
import com.example.geminichatapp.features.text_chat.ChatScreen
import java.util.UUID

@Composable
fun ChatNavHost(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = Destinations.LOGIN.name,
    ) {
        composable(Destinations.LOGIN.name) {
            LoginScreen {
                navHostController.navigate(Destinations.CHANNELS.name)
            }
        }

        composable(Destinations.CHANNELS.name) {
            ChannelListScreen(
                navigateToChatScreen = { passedParam ->
                    navHostController.navigate(Destinations.CHAT.name + "/$passedParam")
                },
                createANewChatThread = { newParamId ->
                    navHostController.navigate(Destinations.CHAT.name + "/$newParamId")
                },
            )
        }

        composable(
            route = Destinations.CHAT.name + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = "0000"
                }
            )
        ) {
            it.arguments?.getString("id")?.let { passedArgument ->
                val channelId = UUID.fromString(passedArgument)
                ChatScreen(channelId = channelId)
            }
        }
    }
}