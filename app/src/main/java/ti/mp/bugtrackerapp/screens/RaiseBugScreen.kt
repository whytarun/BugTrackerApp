package ti.mp.bugtrackerapp.screens

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import ti.mp.bugtrackerapp.utils.GetImageContract
import ti.mp.bugtrackerapp.utils.ImageUtils.getRealPathFromURI
import ti.mp.bugtrackerapp.utils.ImageUtils.getResizedBitmap
import ti.mp.bugtrackerapp.utils.ImageUtils.getStringImage
import ti.mp.bugtrackerapp.viewmodels.BugReportViewModel
import java.io.ByteArrayOutputStream
import java.io.File

@Composable
fun RaiseBugScreen(navController: NavController, viewModel: BugReportViewModel) {

    var bugId by remember { mutableStateOf("") }
    var Description by remember { mutableStateOf("") }
    var userImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var stringImage by remember { mutableStateOf<String?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var file by remember { mutableStateOf<File?>(null) }
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val response by viewModel.response.observeAsState()
    var showMessage by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Take picture launcher
    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                val uri = Uri.fromFile(File(context.cacheDir, "temp.jpg"))
                imageUri = uri
                val bitmap = BitmapFactory.decodeFile(uri.path)
                val resizedBitmap = getResizedBitmap(bitmap, 250)
                stringImage = getStringImage(resizedBitmap)
                userImage = resizedBitmap.asImageBitmap()
            }
        }

    // Photo URI
    val photoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        File(context.cacheDir, "temp.jpg")
    )

     // Pick gallery launcher
    val pickGalleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                imageUri = it
                val realPath = getRealPathFromURI(context, it)
                file = File(realPath)
                val resizedBitmap = getResizedBitmap(bitmap, 250)
                stringImage = getStringImage(resizedBitmap)
                userImage = resizedBitmap.asImageBitmap()
            }
        }

    fun handleImageSelected(uri: Uri) {
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = bugId,
            onValueChange = { bugId = it },
            label = { Text("Bug ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(
            value = Description,
            onValueChange = { Description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        ImagePicker(onImageSelected = { handleImageSelected(it) })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { takePictureLauncher.launch(photoUri) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Take Picture")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { pickGalleryLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Pick from Gallery")
        }

        @Composable
        if (showMessage) {
            Snackbar(
                action = {
                    TextButton(onClick = { showMessage = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(text = "Bug report submitted successfully: $response")
            }
        }

        LaunchedEffect(response) {
            if (!response.isNullOrBlank()) {
                showMessage = true
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(90.dp),
                    color = MaterialTheme.colors.primary
                )
            }
        }

        userImage?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                file?.let {
                    viewModel.submitBugReport(
                        bugId, Description,
                        stringImage!!
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Submit Bug Report")
        }
    }
}
@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    val getContent = rememberLauncherForActivityResult(GetImageContract()) { uri ->
        uri?.let { onImageSelected(it) }
    }
    Button(onClick = { getContent.launch("image/*") }) {
        Text("Select Image")
    }
}





