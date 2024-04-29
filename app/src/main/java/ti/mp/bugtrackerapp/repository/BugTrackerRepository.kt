package ti.mp.bugtrackerapp.repository

import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import ti.mp.bugtrackerapp.api.BugTrackerApi
import ti.mp.bugtrackerapp.utils.AppConstants.APP_SCRIPT_WEB_APP_URL
import javax.inject.Inject

class BugTrackerRepository @Inject constructor(private val requestQueue: RequestQueue) : BugTrackerApi {
    override fun submitBugReport(
        action: String,                         // The action to perform (e.g., insert, update)
        userId: String,                         // The user ID associated with the bug report
        userName: String,                       // The user name associated with the bug report
        userImage: String,                      // The image associated with the bug report (in string format)
        listener: Response.Listener<String>,    // Listener for successful response
        errorListener: Response.ErrorListener   // Listener for error response
    ): StringRequest {
        val url = APP_SCRIPT_WEB_APP_URL                  // URL of the Google Apps Script web app
        val stringRequest = object : StringRequest(Request.Method.POST, url, listener, errorListener) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["action"] = action
                params["uBugId"] = userId
                params["uDescription"] = userName
                params["uImage"] = userImage
                return params
            }
        }

        // Set the retry policy for the request
        val socketTimeout = 30000
        val policy = DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = policy

        // Add the request to the request queue

        requestQueue.add(stringRequest)

        // Return the StringRequest object
        return stringRequest
    }

}