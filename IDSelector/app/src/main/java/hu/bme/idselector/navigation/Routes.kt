package hu.bme.idselector.navigation

sealed class Routes(val route: String) {
    data object ProfileDetails : Routes("profile_details")
    data object ProfileList : Routes("profile_list")
    data object Login: Routes("login")
    data object Registration: Routes("registration")
    data object Authentication: Routes("authentication")
    data object EditNationalId: Routes("edit_national_document")
    data object NewNationalIdDocument: Routes("new_national_document")
    data object NewOtherIdDocument: Routes("new_other_document")
}