package uk.ac.tees.mad.e4552051.pawpal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.*

@Composable
fun PawPalNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavRoutes.SPLASH, modifier = modifier) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(onNavigateToHome = { navController.navigate(NavRoutes.HOME) {
                // clear splash from backstack
                popUpTo(NavRoutes.SPLASH) { inclusive = true }
            }})
        }
        composable(NavRoutes.HOME) {
            HomeScreen(
                onNavigateToAddPet = { navController.navigate(NavRoutes.ADD_PET) },
                onNavigateToReminders = { navController.navigate(NavRoutes.REMINDERS) },
                onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) }
            )
        }
        composable(NavRoutes.ADD_PET) {
            AddPetScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable(NavRoutes.REMINDERS) {
            ReminderScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable(NavRoutes.SETTINGS) {
            SettingsScreen(onNavigateBack = { navController.navigateUp() })
        }
    }
}