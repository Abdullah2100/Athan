package com.example.athan.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.athan.ui.view.Home
import com.example.athan.ui.view.Qeblah
import com.example.athan.ui.view.Setting
import com.example.athan.viewModel.AthanViewModel
import com.example.athan.viewModel.ConnectivityViewModel


@Composable
fun Navigation(
    nav: NavHostController,
    athanViewModel: AthanViewModel,
    connectivityModel: ConnectivityViewModel = hiltViewModel()
    )
{
    NavHost(navController = nav, startDestination = Screens.HomeScreen) {

        composable<Screens.HomeScreen>(
            enterTransition = {
            return@composable fadeIn(tween(200))
        },
            exitTransition = {
        return@composable fadeOut(tween(200))
    }){
            Home(athanViewModel,connectivityModel)
        }

        composable<Screens.SettingScreen>(
            enterTransition = {
                return@composable fadeIn(tween(200))
            },
            exitTransition = {
                return@composable fadeOut(tween(200))
            }){
            Setting(athanViewModel)
        }

        composable<Screens.QeblahScreen>(
            enterTransition = {
                return@composable fadeIn(tween(200))
            },
            exitTransition = {
                return@composable fadeOut(tween(200))
            }){
            Qeblah()
        }

    }
}