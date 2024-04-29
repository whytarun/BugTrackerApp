package ti.mp.bugtrackerapp.di

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ti.mp.bugtrackerapp.api.BugTrackerApi
import ti.mp.bugtrackerapp.repository.BugTrackerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    // Provides a singleton instance of RequestQueue for making network requests
    @Singleton
    @Provides
    fun providesRequestQueue(@ApplicationContext context: Context): RequestQueue {
        return Volley.newRequestQueue(context)
    }

    // Provides a singleton instance of BugTrackerApi for interacting with bug tracking API
    @Singleton
    @Provides
    fun provideBugTrackerApi(requestQueue: RequestQueue): BugTrackerApi {
        return BugTrackerRepository(requestQueue)
    }
}