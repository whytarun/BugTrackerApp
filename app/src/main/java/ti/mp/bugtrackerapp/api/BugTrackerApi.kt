package ti.mp.bugtrackerapp.api


import retrofit2.Response
import retrofit2.http.*

data class ResponseData(val message :String )
interface BugTrackerApi {


    @GET("/macros/s/AKfycbw6APj-ugXBJgL-dop1JxRx_G1Q16xGuQOIHxrFzkA0c0N8cc6ejmN-OBFFfJGCXNXb/exec")
    suspend fun uploadBug(
        @Query("action") action: String,
        @Query("uId") userId: String,
        @Query("uName") userName: String,
        @Query("uImage") userImage: String
    ): ResponseData
}