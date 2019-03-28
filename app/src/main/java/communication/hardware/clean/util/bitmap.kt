package communication.hardware.clean.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

fun Bitmap.scaleRatio(newWidth: Int): Bitmap {
    val aspectRatio = this.width / this.height.toFloat()
    val newHeight = Math.round(newWidth / aspectRatio)
    return this.scale(newWidth, newHeight)
}

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

fun Bitmap.save(path: String, name: String = "${UUID.randomUUID()}.jpg"): Uri =
    Uri.parse(File(path, name).apply {
        FileOutputStream(this).run {
            compress(Bitmap.CompressFormat.JPEG, 100, this)
            flush()
            close()
        }
    }.absolutePath)

fun Bitmap.scale(width: Int, height: Int): Bitmap = Bitmap.createScaledBitmap(this, width, height, false)
fun Bitmap.create(x: Int, y: Int, width: Int, height: Int): Bitmap = Bitmap.createBitmap(this, x, y, width, height)

fun Bitmap.rotate(degress: Float): Bitmap =
    Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { postRotate(degress) }, true)

fun Bitmap.saveInGallery(context: Context): Uri = Uri.parse(
    MediaStore.Images.Media.insertImage(
        context.contentResolver,
        this,
        "${UUID.randomUUID()}.jpg",
        "Electronic Identification"
    )
)