package hu.bme.idselector.navigation

sealed class Routes(val route: String) {
    data object ProfileDetails : Routes("profile_details")
    data object ProfileList : Routes("profile_list")
}