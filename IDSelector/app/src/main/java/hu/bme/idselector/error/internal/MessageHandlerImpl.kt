package hu.bme.idselector.error.internal

import hu.bme.idselector.error.MessageEvent
import hu.bme.idselector.error.MessageHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class MessageHandlerImpl @Inject constructor() : MessageHandler {
    private val messageEvents = MutableSharedFlow<MessageEvent>(extraBufferCapacity = 64)
    override fun handleMessage(event: MessageEvent) {
        messageEvents.tryEmit(event)
    }

    override fun dispatchMessage(): Flow<MessageEvent> = messageEvents
}
