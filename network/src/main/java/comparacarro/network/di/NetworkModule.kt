package comparacarro.network.di

import comparacarro.network.api.CarsApi
import comparacarro.network.api.CarsApiImpl
import comparacarro.network.client.KtorClientProvider
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class NetworkModule {

    @Single
    @Named("baseUrl")
    fun provideBaseUrl(): String = "http://192.168.1.8:8080"

    @Single
    fun provideHttpClient(@Named("baseUrl") baseUrl: String) = KtorClientProvider.create(baseUrl)

    @Single
    fun provideCarsApi(
        httpClient: io.ktor.client.HttpClient,
        @Named("baseUrl") baseUrl: String
    ): CarsApi = CarsApiImpl(httpClient, baseUrl)
}
