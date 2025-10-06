package fe.linksheet.activity.util

interface UiEventReceiver {
    fun receive(event: UiEvent)
    fun send(event: UiEvent) = receive(event)
}
