package ti.mp.bugtrackerapp.repository

import ti.mp.bugtrackerapp.api.BugTrackerApi
import javax.inject.Inject

class BugTrackerRepository @Inject constructor(private val bugTrackerApi: BugTrackerApi) {

    suspend fun uploadBug(){
        //val response =bugTrackerApi.uploadBug()
    }
}