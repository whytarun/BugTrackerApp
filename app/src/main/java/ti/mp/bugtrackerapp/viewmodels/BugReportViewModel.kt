package ti.mp.bugtrackerapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ti.mp.bugtrackerapp.repository.BugTrackerRepository
import javax.inject.Inject

@HiltViewModel
class BugReportViewModel @Inject constructor(private val bugTrackerRepository: BugTrackerRepository) : ViewModel() {

    val  uploadBugReport :StateFlow<String>
    get() = bugTrackerRepository.uploadBugReport
    fun submitBugReport(bugId: String, description: String, userImage: String){
        viewModelScope.launch {
            println("123........"+bugId)
            println("userImage........"+userImage)
            bugTrackerRepository.uploadBugReport(bugId, description,userImage)
        }
    }
}