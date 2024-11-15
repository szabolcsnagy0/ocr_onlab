package hu.bme.idselector.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.idselector.error.MessageEvent
import hu.bme.idselector.error.MessageHandler
import hu.bme.idselector.ui.ProfileDetails
import hu.bme.idselector.ui.ProfileList
import hu.bme.idselector.ui.authentication.LoginScreen
import hu.bme.idselector.ui.authentication.RegistrationScreen
import hu.bme.idselector.ui.createid.NewDocumentFromTemplateScreen
import hu.bme.idselector.ui.createid.NewDocumentScreen
import hu.bme.idselector.ui.createid.NewNationalIdScreen
import hu.bme.idselector.ui.idtemplate.NewTemplateScreen
import hu.bme.idselector.viewmodels.AuthenticationViewModel
import hu.bme.idselector.viewmodels.DocumentListViewModel
import hu.bme.idselector.viewmodels.DocumentTemplateViewModel
import hu.bme.idselector.viewmodels.ProfilesViewModel
import hu.bme.idselector.viewmodels.createid.NewDocumentFromTemplateViewModel
import hu.bme.idselector.viewmodels.createid.NewNationalViewModel
import hu.bme.idselector.viewmodels.createid.NewOtherIdViewModel

@Composable
fun Navigation(messageHandler: MessageHandler) {
    val navController = rememberNavController()
    val authenticationViewModel = viewModel<AuthenticationViewModel>()
    val profilesViewModel = viewModel<ProfilesViewModel>()
    var documentListViewModel: DocumentListViewModel? = null

    authenticationViewModel.testToken()

    NavHost(navController = navController, startDestination = Routes.Authentication.route) {
        navigation(
            startDestination = Routes.Login.route, route = Routes.Authentication.route
        ) {
            composable(route = Routes.Login.route, enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
                )
            }, exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
                )
            }, popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
                )
            }, popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
                )
            }) {
                LoginScreen(
                    navController = navController, viewModel = authenticationViewModel
                )
            }
            composable(route = Routes.Registration.route, enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
                )
            }, exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
                )
            }, popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
                )
            }, popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
                )
            }) {
                RegistrationScreen(navController = navController)
            }
        }

        composable(route = Routes.ProfileList.route, enterTransition = {
            if (this.initialState.destination.route == Routes.ProfileDetails.route
                || this.initialState.destination.route == Routes.NewDocumentTemplate.route
            ) {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
                )
            } else slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, exitTransition = {
            if (this.targetState.destination.route == Routes.ProfileDetails.route
                || this.targetState.destination.route == Routes.NewDocumentTemplate.route
            ) {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
                )
            } else slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }) {
            profilesViewModel.refreshProfilesList()
            ProfileList(viewModel = profilesViewModel, onProfileSelected = {
                profilesViewModel.selectProfile(it)
                navController.navigate(Routes.ProfileDetails.route) {
                    popUpTo(Routes.ProfileDetails.route) {
                        inclusive = true
                    }
                }
            }, logoutRequested = {
                authenticationViewModel.logOut()
                navController.navigate(Routes.Authentication.route) {
                    popUpTo(Routes.Authentication.route) {
                        inclusive = true
                    }
                }
            }, onCreateTemplate = {
                navController.navigate(Routes.NewDocumentTemplate.route) {
                    popUpTo(Routes.NewDocumentTemplate.route) {
                        inclusive = true
                    }
                }
            })
        }
        composable(route = Routes.ProfileDetails.route, enterTransition = {
            if (this.initialState.destination.route == Routes.ProfileList.route) {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
                )
            } else slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }, exitTransition = {
            if (this.targetState.destination.route == Routes.ProfileList.route) {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
                )
            } else slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }) {
            profilesViewModel.selectedProfile.value?.let { profile ->
                documentListViewModel = remember { DocumentListViewModel(profile, navController) }
                documentListViewModel?.let { viewModel ->
                    viewModel.refreshDocumentsList()
                    ProfileDetails(viewModel = viewModel, onBackPressed = {
                        navController.navigate(Routes.ProfileList.route) {
                            popUpTo(Routes.ProfileList.route) {
                                inclusive = true
                            }
                        }
                    })
                }
            }
        }

        composable(route = Routes.NewNationalIdDocument.route, enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }) {
            profilesViewModel.selectedProfile.value?.id?.let { profileId ->
                val newNationalViewModel = remember {
                    NewNationalViewModel(profileId)
                }
                NewNationalIdScreen(viewModel = newNationalViewModel, onCancelled = {
                    newNationalViewModel.cancelUpload()
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                }, onResult = {
                    newNationalViewModel.createId()
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                }, onError = {
                    messageHandler.handleMessage(MessageEvent.Message("Something went wrong, please try again!"))
                })
            }
        }

        composable(route = Routes.NewDocumentFromTemplate.route, enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }, arguments = listOf(navArgument("template_id") { type = NavType.IntType })) { navBackStackEntry ->
            profilesViewModel.selectedProfile.value?.id?.let { profileId ->
                val templateId = navBackStackEntry.arguments?.getInt("template_id") ?: return@let
                val newDocumentFromTemplateViewModel = remember {
                    NewDocumentFromTemplateViewModel(profileId, templateId)
                }
                NewDocumentFromTemplateScreen(viewModel = newDocumentFromTemplateViewModel, onCancelled = {
                    newDocumentFromTemplateViewModel.cancelUpload()
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                }, onResult = {
                    newDocumentFromTemplateViewModel.createId()
                    documentListViewModel?.refreshDocumentsList()
                    navController.navigate(Routes.ProfileDetails.route) {
                        popUpTo(Routes.ProfileDetails.route) {
                            inclusive = true
                        }
                    }
                }, onError = {
                    messageHandler.handleMessage(MessageEvent.Message("Something went wrong, please try again!"))
                })
            }
        }

        composable(route = Routes.NewOtherIdDocument.route, enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }) {
            val newDocumentViewModel = remember {
                NewOtherIdViewModel(profilesViewModel.selectedProfile.value!!.id)
            }
            NewDocumentScreen(viewModel = newDocumentViewModel, onCancelled = {
                newDocumentViewModel.cancelUpload()
                navController.navigate(Routes.ProfileDetails.route) {
                    popUpTo(Routes.ProfileDetails.route) {
                        inclusive = true
                    }
                }
            }, onResult = {
                newDocumentViewModel.onResult()
                navController.navigate(Routes.ProfileDetails.route) {
                    popUpTo(Routes.ProfileDetails.route) {
                        inclusive = true
                    }
                }
            }, onError = {
                messageHandler.handleMessage(MessageEvent.Message("Something went wrong, please try again!"))
            })
        }
        composable(route = Routes.NewDocumentTemplate.route, enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }, popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(700)
            )
        }, popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(700)
            )
        }) {
            val documentTemplateViewModel = remember {
                DocumentTemplateViewModel()
            }
            NewTemplateScreen(viewModel = documentTemplateViewModel, onCancelled = {
                documentTemplateViewModel.cancelUpload()
                navController.navigate(Routes.ProfileList.route) {
                    popUpTo(Routes.ProfileList.route) {
                        inclusive = true
                    }
                }
            }, onResult = {
                navController.navigate(Routes.ProfileList.route) {
                    popUpTo(Routes.ProfileList.route) {
                        inclusive = true
                    }
                }
            }, onError = {
                messageHandler.handleMessage(MessageEvent.Message("Something went wrong, please try again!"))
            })
        }
    }
}
