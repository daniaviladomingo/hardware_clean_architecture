package communication.hardware.clean.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ScheduleProviderImp : IScheduleProvider {
    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}