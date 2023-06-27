package com.example.koltinflowex.common.injection

import com.example.koltinflowex.data.dataImpl.BaseRepoImpl
import com.example.koltinflowex.domain.repository.BaseRepo
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

