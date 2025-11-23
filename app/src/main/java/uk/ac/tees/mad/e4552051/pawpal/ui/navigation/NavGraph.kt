package uk.ac.tees.mad.e4552051.pawpal.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.e4552051.pawpal.data.repository.RepositoryProvider
import androidx.compose.runtime.*
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.splash.SplashScreen
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.home.HomeScreen
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.addpet.AddPetScreen
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.petdetail.PetDetailScreen
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders.ReminderScreen
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.settings.SettingsScreen
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.PetViewModel
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.PetViewModelFactory
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModel
import uk.ac.tees.mad.e4552051.pawpal.ui.viewmodel.ReminderViewModelFactory
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders.AddReminderScreen
import uk.ac.tees.mad.e4552051.pawpal.ui.screens.reminders.ReminderDetailScreen

@Composable
fun PawPalNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    // Create PetViewModel ONCE and share it across multiple screens
    val context = LocalContext.current
    val repository = RepositoryProvider.providePetRepository(context)
    val petViewModel: PetViewModel = viewModel(
        factory = PetViewModelFactory(repository)
    )
    val reminderRepository = RepositoryProvider.provideReminderRepository(context)
    val reminderViewModel: ReminderViewModel = viewModel(
        factory = ReminderViewModelFactory(reminderRepository)
    )

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH,
        modifier = modifier
    ) {

        composable(NavRoutes.SPLASH) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.HOME) {
            HomeScreen(
                viewModel = petViewModel,
                onPetClick = { pet ->
                    navController.navigate("pet_detail/${pet.id}")
                },
                onNavigateToAddPet = { navController.navigate(NavRoutes.ADD_PET) },
                onNavigateToReminders = { navController.navigate(NavRoutes.REMINDERS) },
                onNavigateToSettings = { navController.navigate(NavRoutes.SETTINGS) },
            )
        }

        composable(NavRoutes.ADD_PET) {
            AddPetScreen(
                viewModel = petViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(NavRoutes.REMINDERS) {
            ReminderScreen(
                onNavigateBack = { navController.navigateUp() },
                onReminderClick = { reminder ->
                    navController.navigate("reminder_detail/${reminder.id}")
                },
                onAddReminder = { navController.navigate(NavRoutes.REMINDER_ADD) },
                viewModel = reminderViewModel
            )
        }

        composable(NavRoutes.REMINDER_ADD) {
            AddReminderScreen(
                viewModel = reminderViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable(NavRoutes.SETTINGS) {
            SettingsScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable("pet_detail/{petId}") { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toIntOrNull()

            if (petId == null) {
                // optional: show error or just navigate back
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val pet by petViewModel.getPet(petId).collectAsState(initial = null)

                if (pet != null) {
                    PetDetailScreen(
                        pet = pet!!,
                        viewModel = petViewModel,
                        onNavigateBack = { navController.navigateUp() }
                    )
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        composable("reminder_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                ReminderDetailScreen(
                    reminderId = id,
                    viewModel = reminderViewModel,
                    onNavigateBack = { navController.navigateUp() }
                )
            } else {
                // Show loading UI or placeholder
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}