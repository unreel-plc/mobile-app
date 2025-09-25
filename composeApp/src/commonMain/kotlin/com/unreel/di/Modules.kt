package com.unreel.di
import com.unreel.unreel.MyRepository
import com.unreel.unreel.MyRepositoryImpl
import com.unreel.unreel.MyViewModel
import com.unreel.unreel.ui.feature.auth.login.LoginViewModel
import com.unreel.unreel.core.datastore.OfflineRepository
import com.unreel.unreel.core.datastore.OfflineRepositoryImpl
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import de.jensklingenberg.ktorfit.Ktorfit
import kotlinx.serialization.json.Json
import com.unreel.unreel.core.common.utils.Constants
import com.unreel.unreel.networks.ApiInterface
import com.unreel.unreel.networks.createApiInterface
import com.unreel.unreel.networks.repository.RemoteRepository
import com.unreel.unreel.networks.repository.RemoteRepositoryImpl
import com.unreel.unreel.ui.feature.main.home.HomeViewModel
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging


expect val platformModule: Module

val sharedModule = module {
    singleOf(::MyRepositoryImpl).bind<MyRepository>()
    singleOf(::RemoteRepositoryImpl).bind<RemoteRepository>()
    singleOf(::OfflineRepositoryImpl).bind<OfflineRepository>()
    viewModelOf(::MyViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
}

expect val loginPlatformModule: Module

val networkModule = module {
    single<Json> {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            coerceInputValues = true
        }
    }

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(get<Json>())
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

          /*  defaultRequest {
                headers {
                    append("Content-Type", "application/json")
                    append("Accept", "application/json")
                }
            }*/
        }
    }
   /* single<ImageLoader> {
        ImageLoader.Builder(get<PlatformContext>())
            .components {
                add(KtorNetworkFetcherFactory(httpClient = get<HttpClient>()))
            }
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }*/
   /* single<ImageLoader> {
        ImageLoader.Builder(get<PlatformContext>())
            .logger(DebugLogger())
            .crossfade(true)
            .build()
    }*/

    single<Ktorfit> {
        Ktorfit.Builder()
            .baseUrl(Constants.BASE_URL)
            .httpClient(get<HttpClient>())
            .build()
    }

    single<ApiInterface> {
        get<Ktorfit>().createApiInterface()
    }
}

