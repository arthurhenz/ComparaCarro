package comparacarro.network.di

import comparacarro.network.api.CarImagesApi
import comparacarro.network.api.CarImagesApiImpl
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
    @Named("carImagesBaseUrl")
    fun provideCarImagesBaseUrl(): String = "https://carimagesapi.com"

    @Single
    @Named("carImagesApiKey")
    fun provideCarImagesApiKey(): String = CAR_IMAGES_API_KEY

    @Single
    fun provideHttpClient(): HttpClient = KtorClientProvider.create()

    @Single
    fun provideCarsApi(
        httpClient: HttpClient,
        @Named("baseUrl") baseUrl: String,
    ): CarsApi = CarsApiImpl(httpClient, baseUrl)

    @Single
    fun provideCarImagesApi(
        httpClient: HttpClient,
        @Named("carImagesBaseUrl") baseUrl: String,
        @Named("carImagesApiKey") apiKey: String,
    ): CarImagesApi = CarImagesApiImpl(httpClient, baseUrl, apiKey)

    private companion object {
        // carimagesapi.com public API key. Signing is done server-side via the
        // /api/v1/signed-urls endpoint, so the secret never ships in the app.
        const val CAR_IMAGES_API_KEY = "ci_a494b323c4d8c2e61ccfea6fb4a781247fbf50e368bebc1decfcdf54"
    }
}
