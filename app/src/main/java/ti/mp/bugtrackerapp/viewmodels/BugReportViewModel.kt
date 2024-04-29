package ti.mp.bugtrackerapp.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ti.mp.bugtrackerapp.repository.BugTrackerRepository
import java.io.File
import javax.inject.Inject


/**
 * ViewModel class responsible for handling bug report data and interactions.
 * @param bugTrackerRepository The repository used for interacting with the bug tracking system.
 */
@HiltViewModel
class BugReportViewModel @Inject constructor(private val bugTrackerRepository: BugTrackerRepository) : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    /**
     * Submits a bug report to the bug tracking system.
     * @param bugId The ID of the user submitting the bug report.
     * @param description The name of the user submitting the bug report.
     * @param userImage The image associated with the bug report, encoded as a string.
     */
    fun submitBugReport(bugId: String, description: String, userImage: String) {
        _isLoading.value = true
        val listener = Response.Listener<String> { response ->
            _response.value = response
            _isLoading.value = false
        }

        val errorListener = Response.ErrorListener { error ->
            _response.value = error.message
            _isLoading.value = false
        }

        val action = "insert" // Set the action here as needed
        bugTrackerRepository.submitBugReport(action, bugId, description, userImage, listener, errorListener)
    }
}