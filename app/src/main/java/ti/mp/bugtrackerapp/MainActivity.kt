package ti.mp.bugtrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ti.mp.bugtrackerapp.screens.MainScreen
import ti.mp.bugtrackerapp.screens.RaiseBugScreen
import ti.mp.bugtrackerapp.screens.ViewBugReportsScreen
import ti.mp.bugtrackerapp.ui.theme.BugTrackerAppTheme
import ti.mp.bugtrackerapp.viewmodels.BugReportViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                App()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val bugReportViewModel : BugReportViewModel = viewModel()
    NavHost(navController = navController, startDestination = "mainscreen") {
        composable(route = "mainscreen") {
            MainScreen(navController = navController)
        }
        composable(route = "raisebug") {
            RaiseBugScreen(navController = navController, viewModel = bugReportViewModel)
        }
        composable(route = "viewbugreports") {
            ViewBugReportsScreen(navController = navController)
        }
    }
}





