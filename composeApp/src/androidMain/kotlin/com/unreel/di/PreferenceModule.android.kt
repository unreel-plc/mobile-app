package com.unreel.di

import com.unreel.unreel.core.datastore.createDataStore
import com.unreel.unreel.core.datastore.dataStoreFileName
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferenceModule: Module = module {
    single {
        createDataStore(
            producePath = { androidContext().filesDir.resolve(dataStoreFileName).absolutePath }
        )
    }
}

