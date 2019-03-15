package communication.hardware.clean.device.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.isPermissionGranted(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.isPermissionGranted(permission: List<String>): Boolean =
    permission.none { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }