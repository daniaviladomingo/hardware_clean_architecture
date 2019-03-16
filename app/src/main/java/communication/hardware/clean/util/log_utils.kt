package communication.hardware.clean.util

import android.util.Log
import java.util.*
import kotlin.system.measureTimeMillis

private const val TAG = "LogUtils"

private fun <T> T.asString() = when (this) {
    is Array<*> -> Arrays.toString(this)
    is ByteArray -> Arrays.toString(this)
    is ShortArray -> Arrays.toString(this)
    is IntArray -> Arrays.toString(this)
    is LongArray -> Arrays.toString(this)
    else -> this.toString()
}

/**
 * Log this object as debug.
 */
fun <T> T.logDebug(tag: String = TAG) = apply {
    Log.d(tag, this.asString())
}

/**
 * Log this object as debug (shorter name)
 */
fun <T> T.log(tag: String = TAG) = logDebug(tag)

/**
 * Log this object as info.
 */
fun <T> T.logInfo(tag: String = TAG) = apply {
    Log.i(tag, this.asString())
}

/**
 * Log this object as warning.
 */
fun <T> T.logWarning(tag: String = TAG) = apply {
    Log.w(tag, this.asString())
}


/**
 * Log this object as error.
 */
fun <T> T.logError(tag: String = TAG) = apply {
    Log.e(tag, this.asString())
}

/**
 * Log stacktrace of exception as error
 */
fun <T: Exception> T.log(tag: String = TAG) = apply {
    Log.getStackTraceString(this).logError(tag)
}

/**
 * Executes block and logs execution time for it as debug.
 */
inline fun <T> logExecutionTime(tag: String = "LogUtils", block: () -> T?): T? {
    var value: T? = null
    measureTimeMillis {
        value = block()
    }.apply {
        "$tag: $this".logDebug(tag)
    }
    return value
}


/**
 * Logs the call stack trace as debug.
 */
fun logCallTrace(tag: String = TAG) {
    try {
        throw IllegalStateException()
    } catch (e: IllegalStateException) {
        Log.getStackTraceString(e).logDebug(tag)
    }
}
