package com.apusart.manta.ui.user_module.results

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.apusart.manta.R
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.ui.tools.Tools
import kotlinx.android.synthetic.main.result_details_fragment.*

class ResultDetailsFragment: Fragment(R.layout.result_details_fragment) {
    private val navArgs by navArgs<ResultDetailsFragmentArgs>()
    private val resultsViewModel: ResultsViewModel by viewModels()
    private val personalBestsViewModel: PersonalBestsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        result_details_header.text = getString(R.string.concurence, navArgs.resultData?.distance.toString(), Const.stylesRev.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, Const.courseSize.getString(navArgs.resultData?.res_course_abbr))
        resultsViewModel.results.observe(viewLifecycleOwner, Observer {

        })

        personalBestsViewModel.pb.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                result_details_total_time.text = Tools.convertResult(it[0].res_total_time.toFloat())
            }
        })

        personalBestsViewModel.getPersonalBest(Prefs.getUser()!!.athlete_id, 5, Const.styles.getString(navArgs.resultData?.ss_abbr) ?: navArgs.resultData?.ss_abbr, navArgs.resultData?.distance, navArgs.resultData?.res_course_abbr)
        resultsViewModel.getResultsByAthleteId(Prefs.getUser()!!.athlete_id, 300, navArgs.resultData?.ss_abbr, navArgs.resultData?.distance)
    }
}