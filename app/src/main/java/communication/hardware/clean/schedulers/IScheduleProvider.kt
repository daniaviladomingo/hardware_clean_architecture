package communication.hardware.clean.schedulers

import io.reactivex.Scheduler

interface IScheduleProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
}