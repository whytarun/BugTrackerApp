package ti.mp.bugtrackerapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ti.mp.bugtrackerapp.api.BugTrackerApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit() :Retrofit{
        return Retrofit.Builder().baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideBugTrackerApi(retrofit: Retrofit) :BugTrackerApi{
        return retrofit.create(BugTrackerApi::class.java)
    }
}