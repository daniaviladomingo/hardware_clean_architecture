package communication.hardware.clean.model.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import communication.hardware.clean.domain.camera.model.Picture
import communication.hardware.clean.domain.model.mapper.Mapper

class PictureMapper : Mapper<Picture, Bitmap>() {

    override fun inverseMap(model: Bitmap): Picture {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun map(model: Picture): Bitmap = model.run {
        Bitmap.createBitmap(
            BitmapFactory.decodeByteArray(this.data, 0, this.data.size),
            0,
            0,
            width,
            height,
            Matrix().apply { postRotate(this@run.rotation.toFloat()) },
            true
        )
    }
}