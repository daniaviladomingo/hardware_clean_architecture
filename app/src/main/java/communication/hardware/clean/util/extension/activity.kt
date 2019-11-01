package communication.hardware.clean.util.extension

import android.app.Activity
import androidx.core.app.ActivityCompat

fun Activity.requestPermission(permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
}

fun Activity.requestPermission(permission: List<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(this, permission.toTypedArray(), requestCode)
}