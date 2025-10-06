package fe.linksheet.module.resolver

sealed interface ResolverInteraction {
    data object Idle : ResolverInteraction
    data object Initialized : ResolverInteraction
    data object Clear : ResolverInteraction
    data class Cancelable(val event: ResolveEvent, val cancel: () -> Unit) : ResolverInteraction
}

enum class ResolveEvent {
    Idle,
    Initialized,
    QueryingBrowsers,
    ApplyingLinkModifiers,
    ResolvingRedirects,
    RunningAmp2Html,
    CheckingLibRedirect,
    CheckingDownloader,
    LoadingPreferredApps,
    CheckingBrowsers,
    SortingApps,
    GeneratingPreview
}
