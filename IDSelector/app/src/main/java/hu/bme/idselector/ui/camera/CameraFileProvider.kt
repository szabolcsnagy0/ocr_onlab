package hu.bme.idselector.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import hu.bme.idselector.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class CameraFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {

        /**
         * Returns a pair of Uri and path for the image file.
         * @param context Context
         * @return Pair<Uri?, String?> Uri and path for the image file.
         */
        fun getImageUriAndPath(context: Context): Pair<Uri?, String?> {
            var uri: Uri? = null
            val file = createImageFile(context)
            val authority = context.packageName + ".fileprovider"
            try {
                uri = getUriForFile(context, authority, file)
            } catch (_: Exception) {
            }
            return Pair(uri, file.absolutePath)
        }


        /**
         * Creates a temporary image file.
         * @param context Context
         * @return File Temporary image file.
         */
        @SuppressLint("SimpleDateFormat")
        private fun createImageFile(context: Context): File {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                "QTYADOKI_IMAGE_${timestamp}",
                ".jpg",
                imageDirectory
            )
        }
    }
}