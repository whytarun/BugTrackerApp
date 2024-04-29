package ti.mp.bugtrackerapp.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtils {
    // Implementation of getResizedBitmap function
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

    // Implementation of getStringImage function
    fun getStringImage(bmp: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    // Implementation of getRealPathFromURI function
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
}