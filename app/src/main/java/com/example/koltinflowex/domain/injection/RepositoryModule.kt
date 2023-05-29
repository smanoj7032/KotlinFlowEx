package com.example.koltinflowex.domain.injection

import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.domain.repository.BaseRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideRepo(provideImpl: BaseRepoImpl): BaseRepo = provideImpl
}

