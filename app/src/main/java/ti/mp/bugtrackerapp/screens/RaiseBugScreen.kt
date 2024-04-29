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
    var   file  by remember { mutableStateOf<File?>(null) }
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    val context = LocalContext.current

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            val uri = Uri.fromFile(File(context.cacheDir, "temp.jpg"))
            imageUri = uri
            val bitmap = BitmapFactory.decodeFile(uri.path)
            val resizedBitmap = getResizedBitmap(bitmap, 250)
            stringImage = getStringImage(resizedBitmap)
            userImage = resizedBitmap.asImageBitmap()
        }
    }

    val photoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        File(context.cacheDir, "temp.jpg")
    )

    val pickGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
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
                    viewModel.addUser(bugId, Description,
                        stringImage!!
                    )
                }
             },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Raise Bug")
        }
    }
}

fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
    val width = image.width
    val height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    val finalWidth: Int
    val finalHeight: Int
    if (bitmapRatio > 1) {
        finalWidth = maxSize
        finalHeight = (finalWidth / bitmapRatio).toInt()
    } else {
        finalHeight = maxSize
        finalWidth = (finalHeight * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
}

fun getStringImage(bmp: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val imageBytes = baos.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}

fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        columnIndex?.let { cursor?.getString(it) }
    } finally {
        cursor?.close()
    }
}
