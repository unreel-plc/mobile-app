package com.unreel.unreel

import androidx.lifecycle.ViewModel
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.networks.repository.RemoteRepository

class MyViewModel(
    private val repository: MyRepository,
    private val remoteRepository: RemoteRepository,
    private val offlineRepository: OfflineRepository
): ViewModel() {

    fun getHelloWorldString(): String {
        return repository.helloWorld()
    }
}

interface MyRepository {
    fun helloWorld(): String
}

class MyRepositoryImpl(
    private val dbClient: DbClient
) : MyRepository {
    override fun helloWorld(): String {
        return "Hello World!"
    }
}

expect class DbClient