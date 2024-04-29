package ti.mp.bugtrackerapp.models

data class BugReport(
    val bugId: String,        // The unique identifier for the bug report
    val description: String,  // The description of the bug
    val image:String          // The image associated with the bug report (in string format)
)
