package ti.mp.bugtrackerapp.di

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ti.mp.bugtrackerapp.api.BugTrackerApi
import ti.mp.bugtrackerapp.repository.BugTrackerRepository
import ti.mp.bugtrackerapp.utils.AppConstants.BASE_URL
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesRequestQueue(@ApplicationContext context: Context): RequestQueue {
        return Volley.newRequestQueue(context)
    }

    @Singleton
    @Provides
    fun provideBugTrackerApi(requestQueue: RequestQueue): BugTrackerApi {
        return BugTrackerRepository(requestQueue)
    }
}