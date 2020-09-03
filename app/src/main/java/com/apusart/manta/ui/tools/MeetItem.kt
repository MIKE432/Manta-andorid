package com.apusart.manta.ui.tools

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.apusart.manta.R
import kotlinx.android.synthetic.main.meet_item.view.*

class MeetItem(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {
    private val mLayout = LayoutInflater.from(context)
        .inflate(R.layout.meet_item, this, false)

    var mMeetTitle: String = ""
        set(value) {
            mLayout.meet_item_meet_title.text = value
            field = value
        }

    var mMeetCity: String = ""
        set(value) {
            mLayout.meet_item_meet_city.text = value
            field = value
        }

    var mMeetCourse: String = ""
        set(value) {
            mLayout.meet_item_course.text = value
            field = value
        }
    var mMeetDate: String = ""
        set(value) {
            mLayout.meet_item_date.text = value
            field = value
        }

    init {
        addView(mLayout)
    }

}