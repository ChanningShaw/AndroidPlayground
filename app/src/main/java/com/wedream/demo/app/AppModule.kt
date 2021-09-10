package com.wedream.demo.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    @Named("String1")
    fun provideTestString1() = "This is a test string"

    @Singleton
    @Provides
    @Named("String2")
    fun provideTestString2() = "This is a test string2"
}