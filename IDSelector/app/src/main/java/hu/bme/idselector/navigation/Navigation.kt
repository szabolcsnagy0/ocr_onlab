package hu.bme.idselector.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import hu.bme.idselector.ui.authentication.RegistrationScreen
import hu.bme.idselector.api.TokenManager
import hu.bme.idselector.ui.ProfileDetails
import hu.bme.idselector.ui.ProfileList
import hu.bme.idselector.ui.authentication.LoginScreen
import hu.bme.idselector.ui.createid.NewDocumentScreen
import hu.bme.idselector.ui.createid.NewNationalIdScreen
import hu.bme.idselector.viewmodels.AuthenticationViewModel
import hu.bme.idselector.viewmodels.DocumentListViewModel
import hu.bme.idselector.viewmodels.createid.NewNationalViewModel
import hu.bme.idselector.viewmodels.createid.NewOtherIdViewModel
import hu.bme.idselector.viewmodels.ProfilesViewModel

@Composable
fun Navigation(tokenManager: TokenManager) {
    val navController = rememberNavController()
    val authenticationViewModel = remember { AuthenticationViewModel(tokenManager) }
    val profilesViewModel = remember { ProfilesViewModel() }

    authenticationViewModel.testToken()

    NavHost(navController = navController, startDestination = Routes.Authentication.route) {
        navigation(
            startDestination = Routes.Login.route,
            route = Routes.Authentication.route
        ) {
            composable(route = Routes.Login.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                }) {
                LoginScreen(
                    navController = navController,
                    viewModel = authenticationViewModel
                )
            }
            composable(route = Routes.Registration.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                }) {
                RegistrationScreen(navController = navController)
            }
        }

        composable(route = Routes.ProfileList.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            profilesViewModel.refreshProfilesList()
            ProfileList(viewModel = profilesViewModel,
                onProfileSelected = {
                    profilesViewModel.selectProfile(it)
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                },
                logoutRequested = {
                    authenticationViewModel.logOut()
                    navController.navigate(Routes.Authentication.route) {
                        popUpTo(Routes.Authentication.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Routes.ProfileDetails.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }) {
            profilesViewModel.selectedProfile.value?.let {
                val viewModel = remember { DocumentListViewModel(it) }
                ProfileDetails(
                    viewModel = viewModel,
                    onBackPressed = {
                        navController.navigate(Routes.ProfileList.route) {
                            popUpTo(Routes.ProfileList.route) {
                                inclusive = true
                            }
                        }
                    },
                    addNewNationalIdDocument = {
                        navController.navigate(Routes.NewNationalIdDocument.route)
                    },
                    addNewOtherIdDocument = {
                        navController.navigate(Routes.NewOtherIdDocument.route)
                    }
                )
            }
        }

        composable(route = Routes.NewNationalIdDocument.route) {
            val newNationalViewModel = remember {
                NewNationalViewModel(profilesViewModel.selectedProfile.value!!.id)
            }
            NewNationalIdScreen(
                viewModel = newNationalViewModel,
                onCancelled = {
                    newNationalViewModel.cancelUpload()
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                },
                onResult = {
                    newNationalViewModel.createId()
                    profilesViewModel.refreshProfilesList()
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.NewOtherIdDocument.route) {
            val newDocumentViewModel = remember {
                NewOtherIdViewModel(profilesViewModel.selectedProfile.value!!.id)
            }
            NewDocumentScreen(
                viewModel = newDocumentViewModel,
                onCancelled = {
                    newDocumentViewModel.cancelUpload()
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                },
                onResult = {
                    newDocumentViewModel.onResult()
                    profilesViewModel.refreshProfilesList()
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}