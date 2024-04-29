package ti.mp.bugtrackerapp.api


import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import retrofit2.http.*
import ti.mp.bugtrackerapp.utils.AppConstants

interface BugTrackerApi {

    @POST(AppConstants.APP_SCRIPT_WEB_APP_URL)
    fun addUser(
        @Query("action") action: String,
        @Query("uBugId") userId: String,
        @Query("uDescription") userName: String,
        @Query("uImage") userImage: String,
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest
}