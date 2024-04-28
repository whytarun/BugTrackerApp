package ti.mp.bugtrackerapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ti.mp.bugtrackerapp.api.BugTrackerApi
import ti.mp.bugtrackerapp.utils.AppConstants.BASE_URL
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
        level = HttpLoggingInterceptor.Level.HEADERS
    }
    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient) :Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideBugTrackerApi(retrofit: Retrofit) :BugTrackerApi{
        return retrofit.create(BugTrackerApi::class.java)
    }
}