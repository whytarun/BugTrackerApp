package ti.mp.bugtrackerapp.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ti.mp.bugtrackerapp.api.BugTrackerApi
import javax.inject.Inject

class BugTrackerRepository @Inject constructor(private val bugTrackerApi: BugTrackerApi) {

    private val _uploadBugReport = MutableStateFlow<String>("")
    val uploadBugReport :StateFlow<String>
    get() = _uploadBugReport
    suspend fun uploadBugReport(bugId: String, description: String, userImage: String){
        //println("data...."+bugId + description+ "----"+ userImage)
        val response =bugTrackerApi.uploadBug("insert",bugId, description,userImage )
        println("sucesss....."+response)
        if(response.message == "Success"){
            println(" respones is success ")
            _uploadBugReport.emit("success")
        }else {
            // Handle HTTP status code indicating failure
            println("Response is not successful. Status code: ${response}")
        }
    }
}