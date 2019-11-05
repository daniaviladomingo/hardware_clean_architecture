package communication.hardware.clean.device.location.mode.mapper

import com.google.android.gms.location.LocationRequest
import communication.hardware.clean.device.location.mode.Accuracy
import communication.hardware.clean.domain.model.mapper.Mapper

class AccuracyMapper : Mapper<Accuracy, Int>() {
    override fun map(model: Accuracy): Int = when (model) {
        Accuracy.PRIORITY_BALANCED_POWER_ACCURACY -> LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        Accuracy.PRIORITY_HIGH_ACCURACY -> LocationRequest.PRIORITY_HIGH_ACCURACY
        Accuracy.PRIORITY_LOW_POWER -> LocationRequest.PRIORITY_LOW_POWER
        Accuracy.PRIORITY_NO_POWER -> LocationRequest.PRIORITY_NO_POWER
    }

    override fun inverseMap(model: Int): Accuracy {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}