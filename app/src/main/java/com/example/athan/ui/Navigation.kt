package com.example.athan.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun Navigation(nav: NavHostController)
{
    NavHost(navController = nav, startDestination = Screens.HomeScreen) {

        composable<Screens.HomeScreen>(
            enterTransition = {
            return@composable fadeIn(tween(200))
        },
            exitTransition = {
        return@composable fadeOut(tween(200))
    }){}

        composable<Screens.SettingScreen>(
            enterTransition = {
                return@composable fadeIn(tween(200))
            },
            exitTransition = {
                return@composable fadeOut(tween(200))
            }){}

        composable<Screens.QeblahScreen>(
            enterTransition = {
                return@composable fadeIn(tween(200))
            },
            exitTransition = {
                return@composable fadeOut(tween(200))
            }){}

    }
}