package ti.mp.bugtrackerapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.POST
import retrofit2.http.Part

interface BugTrackerApi {
    @POST("uploadBug")
    suspend fun uploadBug(
        @Part("bugId") bugId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Boolean
}