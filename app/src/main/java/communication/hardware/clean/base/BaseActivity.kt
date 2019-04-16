package communication.hardware.clean.base

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import communication.hardware.clean.R
import communication.hardware.clean.ui.data.ResourceState
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.view_error.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class BaseActivity : AppCompatActivity() {

    private val fakeInject: Unit by inject { parametersOf(this) }

    private lateinit var activityView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fakeInject.apply {  }

        if (getLayoutId() == 0) {
            throw RuntimeException("Invalid Layout ID")
        }

        setContentView(R.layout.activity_base)

        activityView = layoutInflater.inflate(getLayoutId(), null)
        (view as FrameLayout).addView(activityView, LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        view_empty.emptyListener = checkAgain()
        view_error.errorListener = tryAgain()

        initializeToolbar()
    }

    private fun initializeToolbar() {}

    protected abstract fun getLayoutId(): Int

    protected fun managementResourceState(resourceState: ResourceState, message: String?) {
        when (resourceState) {
            ResourceState.LOADING -> {
                view.visibility = VISIBLE
                view_error.visibility = GONE
                view_empty.visibility = GONE
                view_progress.visibility = VISIBLE
            }
            ResourceState.SUCCESS -> {
                view.visibility = VISIBLE
                view_error.visibility = GONE
                view_empty.visibility = GONE
                view_progress.visibility = GONE
            }
            ResourceState.EMPTY -> {
                view.visibility = GONE
                view_error.visibility = GONE
                view_empty.visibility = VISIBLE
                view_progress.visibility = GONE
            }
            ResourceState.ERROR -> {
                view.visibility = GONE
                view_error.visibility = VISIBLE
                error_message.text = message ?: ""
                view_empty.visibility = GONE
                view_progress.visibility = GONE
            }
        }
    }

    abstract fun checkAgain(): () -> Unit

    abstract fun tryAgain(): () -> Unit
}