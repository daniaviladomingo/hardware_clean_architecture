package communication.hardware.clean.model.mapper

import communication.hardware.clean.domain.model.mapper.Mapper
import java.text.SimpleDateFormat
import java.util.*

class ShakeMapper(
    private val simpleDateFormat: SimpleDateFormat
) : Mapper<Unit, String>() {
    override fun inverseMap(model: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun map(model: Unit): String = simpleDateFormat.format(Date())
}