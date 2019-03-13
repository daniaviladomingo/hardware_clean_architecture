package communication.hardware.clean.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import communication.hardware.clean.R
import kotlinx.android.synthetic.main.view_empty.view.*

class EmptyView : RelativeLayout {

    var emptyListener: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.view_empty, this)
        button_check_again.setOnClickListener { emptyListener?.invoke() }
    }
}