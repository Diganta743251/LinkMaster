package fe.linksheet.activity.util

sealed class UiEvent {
    data class ShowSnackbar(val text: String) : UiEvent()
    data class NavigateTo(val route: String) : UiEvent()
}
