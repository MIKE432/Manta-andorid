package com.apusart.manta.ui.tools

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.apusart.manta.R
import com.apusart.manta.api.models.Record
import kotlinx.android.synthetic.main.record_list_item.view.*

data class RecordListItemData(val styleName: String, val m50: Record?, val m100: Record?, val m200: Record?, val m400: Record?, val m800: Record?, val m1500: Record?)

class RecordListItem(context: Context): LinearLayout(context) {
    private val mView = LayoutInflater.from(context)
        .inflate(R.layout.record_list_item, this, false)

    init {
        addView(mView)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun bind(data: RecordListItemData) {
        mView.record_list_item_style.text = data.styleName

        if(data.m50 == null) {
            mView.record_list_item_50_container.isVisible = false
        } else {
            mView.record_list_item_50_time.text = Tools.convertResult(data.m50.rsb_time.toFloat())
            mView.record_list_item_50_points.text = "500"
            mView.record_list_item_50_athlete.text = data.m50.rsb_athlete
            mView.record_list_item_50_place.text = data.m50.mt_place
            mView.record_list_item_50_date.text = data.m50.rsb_meet_date
            mView.record_list_item_50_age.text = data.m50.rsb_age.toString()
        }

        if(data.m100 == null) {
            mView.record_list_item_100_container.isVisible = false
        } else {
            mView.record_list_item_100_time.text = Tools.convertResult(data.m100.rsb_time.toFloat())
            mView.record_list_item_100_points.text = "500"
            mView.record_list_item_100_athlete.text = data.m100.rsb_athlete
            mView.record_list_item_100_place.text = data.m100.mt_place
            mView.record_list_item_100_date.text = data.m100.rsb_meet_date
            mView.record_list_item_100_age.text = data.m100.rsb_age.toString()
        }

        if(data.m200 == null) {
            mView.record_list_item_200_container.isVisible = false
        } else {
            mView.record_list_item_200_time.text = Tools.convertResult(data.m200.rsb_time.toFloat())
            mView.record_list_item_200_points.text = "500"
            mView.record_list_item_200_athlete.text = data.m200.rsb_athlete
            mView.record_list_item_200_place.text = data.m200.mt_place
            mView.record_list_item_200_date.text = data.m200.rsb_meet_date
            mView.record_list_item_200_age.text = data.m200.rsb_age.toString()
        }

        if(data.m400 == null) {
            mView.record_list_item_400_container.isVisible = false
        } else {
            mView.record_list_item_400_time.text = Tools.convertResult(data.m400.rsb_time.toFloat())
            mView.record_list_item_400_points.text = "500"
            mView.record_list_item_400_athlete.text = data.m400.rsb_athlete
            mView.record_list_item_400_place.text = data.m400.mt_place
            mView.record_list_item_400_date.text = data.m400.rsb_meet_date
            mView.record_list_item_400_age.text = data.m400.rsb_age.toString()
        }

        if(data.m800 == null) {
            mView.record_list_item_800_container.isVisible = false
        } else {
            mView.record_list_item_800_time.text = Tools.convertResult(data.m800.rsb_time.toFloat())
            mView.record_list_item_800_points.text = "500"
            mView.record_list_item_800_athlete.text = data.m800.rsb_athlete
            mView.record_list_item_800_place.text = data.m800.mt_place
            mView.record_list_item_800_date.text = data.m800.rsb_meet_date
            mView.record_list_item_800_age.text = data.m800.rsb_age.toString()
        }

        if(data.m1500 == null) {
            mView.record_list_item_1500_container.isVisible = false
        } else {
            mView.record_list_item_1500_time.text = Tools.convertResult(data.m1500.rsb_time.toFloat())
            mView.record_list_item_1500_points.text = "500"
            mView.record_list_item_1500_athlete.text = data.m1500.rsb_athlete
            mView.record_list_item_1500_place.text = data.m1500.mt_place
            mView.record_list_item_1500_date.text = data.m1500.rsb_meet_date
            mView.record_list_item_1500_age.text = data.m1500.rsb_age.toString()
        }
    }
}