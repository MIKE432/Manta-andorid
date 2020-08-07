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
import kotlinx.android.synthetic.main.graph.view.*
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

        result_details_graph.graph_graph.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                result_details_graph.setDim(result_details_graph.graph_graph.height.toFloat(), result_details_graph.graph_graph.width.toFloat())
                result_details_graph.graph_graph.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        resultsViewModel.results.observe(viewLifecycleOwner, Observer {
            result_details_graph.resetData()
            it.forEach { res ->

                result_details_graph.addData(DataPoint(Tools.sdf.parse(res.mt_from)?.time?.toFloat() ?: 0f, res.res_total_time?.toFloat() ?: 0f, res))
            }

            result_details_graph.applyData()
        })

        personalBestsViewModel.pb.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                result_details_total_time.text = Tools.convertResult(it[0].res_total_time.toFloat())
                result_details_points.text = it[0].res_points.toString()
                result_details_res_split_times.text = it[0].res_split_times
                result_details_res_count_value.text = it[0].res_count.toString()

                result_details_res_split_times_header.isVisible = it[0].sev_distance > 50
            }
        })

        resultsViewModel.dsqCount.observe(viewLifecycleOwner, Observer {
            result_details_dsq_value.text = it.toString()
        })

        result_details_refresher.setOnRefreshListener {
//            result_details_res_split_times_container.removeAllViews()
            personalBestsViewModel.getPersonalBest(Prefs.getUser()!!.athlete_id, 5, Const.styles.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, navArgs.resultData?.distance, navArgs.resultData?.res_course_abbr)
            resultsViewModel.getResultsByAthleteId(Prefs.getUser()!!.athlete_id, 300, Const.styles.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, navArgs.resultData?.distance, navArgs.resultData?.res_course_abbr)
            result_details_refresher.isRefreshing = false
        }

        personalBestsViewModel.getPersonalBest(Prefs.getUser()!!.athlete_id, 1, Const.styles.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, navArgs.resultData?.distance, navArgs.resultData?.res_course_abbr)
        resultsViewModel.getResultsByAthleteId(Prefs.getUser()!!.athlete_id, 300, Const.styles.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, navArgs.resultData?.distance, navArgs.resultData?.res_course_abbr)
    }
}