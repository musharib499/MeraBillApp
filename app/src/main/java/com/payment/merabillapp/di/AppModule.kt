package com.payment.merabillapp.di

import android.app.Application
import com.google.gson.Gson
import com.payment.merabillapp.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providePaymentRepository(application: Application, gson: Gson): PaymentRepository =
        PaymentRepository(application, gson)
}