package communication.hardware.clean.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64

fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)
fun ByteArray.toBitmap(): Bitmap = BitmapFactory.decodeByteArray(this, 0, size)
fun ByteArray.toBitmapRotate(degrees: Int): Bitmap = BitmapFactory.decodeByteArray(this, 0, size).run {
    Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { postRotate(degrees.toFloat()) }, true)
}