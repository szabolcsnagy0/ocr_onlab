package hu.bme.idselector.navigation

sealed class Routes(val route: String) {
    data object ProfileDetails : Routes("profile_details")
    data object ProfileList : Routes("profile_list")
    data object Login : Routes("login")
    data object Registration : Routes("registration")
    data object Authentication : Routes("authentication")
    data object NewNationalIdDocument : Routes("new_national_document")
    data object NewOtherIdDocument : Routes("new_other_document")
    data object NewDocumentFromTemplate : Routes("new_document_from_template/{template_id}")
    data object NewDocumentTemplate : Routes("new_document_template")
}
