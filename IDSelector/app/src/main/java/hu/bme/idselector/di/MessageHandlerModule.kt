package hu.bme.idselector.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.idselector.error.MessageHandler
import hu.bme.idselector.error.internal.MessageHandlerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MessageHandlerModule {

    @Singleton
    @Binds
    internal abstract fun provideMessageHandler(messageHandler: MessageHandlerImpl): MessageHandler
}
