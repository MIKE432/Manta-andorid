package com.apusart.manta.ui.user_module.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.apusart.manta.R
import com.apusart.manta.api.models.PersonalBest
import com.apusart.manta.ui.tools.*
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.result_details_fragment.*
import kotlinx.android.synthetic.main.result_split_time_item.view.*
import java.util.*

class ResultDetailsFragment: Fragment(R.layout.result_details_fragment) {
    private val navArgs by navArgs<ResultDetailsFragmentArgs>()
    private val resultsViewModel: ResultsViewModel by viewModels()
    private val personalBestsViewModel: PersonalBestsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result_details_header.text = getString(R.string.concurence, navArgs.resultData?.distance.toString(), Const.stylesRev.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, Const.courseSize.getString(navArgs.resultData?.res_course_abbr))

        result_details_graph.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                result_details_graph.setDim(result_details_graph.height.toFloat(), result_details_graph.width.toFloat())
                result_details_graph.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }

        })

        resultsViewModel.results.observe(viewLifecycleOwner, Observer {
            //-------------------- Handle Graph ---------------------------
//            series1 = LineGraphSeries<DataPoint>()
//            val dataPoints = ArrayList<DataPoint>()
//            it.forEach { pb ->
//                val x = Tools.sdf.parse(pb.mt_from)
//                val y = pb.res_total_time?.toDouble() ?: 0.0
//                dataPoints.add(DataPoint(x, y))
//            }
//            dataPoints.sortBy { dp -> dp.x }
//            dataPoints.forEach { dp ->
//                series1.appendData(dp, false, 100)
//            }
//            graph?.addSeries(series1)

            it.forEach { res ->

                result_details_graph.addData(DataPoint(
                    Tools.sdf.parse(res.mt_from)?.time?.toFloat()?.div(10000000f) ?: 0f, res.res_total_time?.toFloat() ?: 0f))
            }
//            private fun globalLayoutListener(view: View, it: PersonalBestViewHolder) {
//        view.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                it.mHeight = itemView.personal_best_item_additional_info.measuredHeight
//                view.personal_best_item_additional_info.layoutParams.height = 0
//                view.personal_best_item_additional_info.requestLayout()
//                view.viewTreeObserver.removeOnGlobalLayoutListener(this);
//            }
//        })
//    }
            result_details_graph.applyData()

        })

        personalBestsViewModel.pb.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                result_details_total_time.text = Tools.convertResult(it[0].res_total_time.toFloat())
                result_details_points.text = it[0].res_points.toString()
                result_details_res_split_times.text = it[0].res_split_times
                result_details_res_count_value.text = it[0].res_count.toString()
                result_details_date_value.text = it[0].mt_from
                result_details_city_value.text = it[0].mt_city




//                val splittedLaps = Tools.splitLaps(it[0].res_split_times)
//                splittedLaps.forEach { s ->
//                    val splitLayout = TextView(this.context)
//                    splitLayout.setTextAppearance(R.style.Roboto14Pt)
//
//                    result_details_res_split_times_container.addView(splitLayout)
//                    splitLayout.text = s
//                }

//                result_details_res_split_times_container.isVisible = splittedLaps.isNotEmpty()
            }
        })

        result_details_refresher.setOnRefreshListener {
//            result_details_res_split_times_container.removeAllViews()
            personalBestsViewModel.getPersonalBest(Prefs.getUser()!!.athlete_id, 5, Const.styles.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, navArgs.resultData?.distance, navArgs.resultData?.res_course_abbr)
            resultsViewModel.getResultsByAthleteId(Prefs.getUser()!!.athlete_id, 300, navArgs.resultData?.ss_abbr, navArgs.resultData?.distance)
            result_details_refresher.isRefreshing = false
        }

        personalBestsViewModel.getPersonalBest(Prefs.getUser()!!.athlete_id, 5, Const.styles.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, navArgs.resultData?.distance, navArgs.resultData?.res_course_abbr)
        resultsViewModel.getResultsByAthleteId(Prefs.getUser()!!.athlete_id, 300, navArgs.resultData?.ss_abbr, navArgs.resultData?.distance, navArgs.resultData?.res_course_abbr)
    }
}