package hu.bme.idselector.error

import kotlinx.coroutines.flow.Flow

interface MessageHandler {
    fun handleMessage(event: MessageEvent)
    fun dispatchMessage(): Flow<MessageEvent>
}
