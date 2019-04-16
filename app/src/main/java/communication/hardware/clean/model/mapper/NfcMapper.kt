package communication.hardware.clean.model.mapper

import communication.hardware.clean.domain.model.mapper.Mapper

class NfcMapper : Mapper<ByteArray, String>() {
    override fun map(model: ByteArray): String = model.toString()

    override fun inverseMap(model: String): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}