package comparacarro.network.di

import comparacarro.network.api.CarsApi
import comparacarro.network.api.CarsApiImpl
import comparacarro.network.client.KtorClientProvider
import io.ktor.client.HttpClient
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class NetworkModule {
    @Single
    @Named("baseUrl")
    fun provideBaseUrl(): String = "https://api.fipex.com.br/v1"

    @Single
    fun provideHttpClient(): HttpClient = KtorClientProvider.create()

    @Single
    fun provideCarsApi(
        httpClient: HttpClient,
        @Named("baseUrl") baseUrl: String,
    ): CarsApi = CarsApiImpl(httpClient, baseUrl)
}
