package comparacarro2.network.di

import comparacarro2.network.api.CarsApi
import comparacarro2.network.api.CarsApiImpl
import comparacarro2.network.client.KtorClientProvider
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class NetworkModule {

    @Single
    @Named("baseUrl")
    fun provideBaseUrl(): String = "http://127.0.0.1:8080"

    @Single
    fun provideHttpClient(@Named("baseUrl") baseUrl: String) = KtorClientProvider.create(baseUrl)

    @Single
    fun provideCarsApi(
        httpClient: io.ktor.client.HttpClient,
        @Named("baseUrl") baseUrl: String
    ): CarsApi = CarsApiImpl(httpClient, baseUrl)
}
