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
    override fun addUser(
        action: String,
        userId: String,
        userName: String,
        userImage: String,
        listener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        val url = APP_SCRIPT_WEB_APP_URL
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
        val socketTimeout = 30000
        val policy = DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = policy
        requestQueue.add(stringRequest)
        return stringRequest
    }

}