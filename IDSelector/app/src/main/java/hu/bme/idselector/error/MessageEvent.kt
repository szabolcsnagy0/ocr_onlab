package hu.bme.idselector.error

sealed class MessageEvent {
    class Error(val throwable: Throwable) : MessageEvent()
    class Message(val message: String) : MessageEvent()
}
