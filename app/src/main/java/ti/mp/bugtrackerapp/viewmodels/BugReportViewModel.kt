package ti.mp.bugtrackerapp.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ti.mp.bugtrackerapp.repository.BugTrackerRepository
import javax.inject.Inject

@HiltViewModel
class BugReportViewModel @Inject constructor(private val bugTrackerRepository: BugTrackerRepository) : ViewModel() {

}