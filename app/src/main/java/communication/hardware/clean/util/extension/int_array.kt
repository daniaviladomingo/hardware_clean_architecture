package communication.hardware.clean.util.extension

import android.content.pm.PackageManager

fun IntArray.isPermissionsGranted(): Boolean = none { it != PackageManager.PERMISSION_GRANTED }
