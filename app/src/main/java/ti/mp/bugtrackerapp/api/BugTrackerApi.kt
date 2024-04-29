package ti.mp.bugtrackerapp.api


import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import retrofit2.http.*
import ti.mp.bugtrackerapp.utils.AppConstants

interface BugTrackerApi {
    // POST request to submit a bug report
    @POST(AppConstants.APP_SCRIPT_WEB_APP_URL)
    fun submitBugReport(
        @Query("action") action: String,                 // Action parameter for the request
        @Query("uBugId") userId: String,                 // Bug ID of the report
        @Query("uDescription") userName: String,        // Description of the bug
        @Query("uImage") userImage: String,            // Image of the bug (as string)
        listener: Response.Listener<String>,          // Success listener for the response
        errorListener: Response.ErrorListener        // Error listener for the response
    ): StringRequest
}