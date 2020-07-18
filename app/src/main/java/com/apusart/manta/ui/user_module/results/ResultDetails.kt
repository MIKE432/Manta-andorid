package com.apusart.manta.ui.user_module.results

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.apusart.manta.R
import kotlinx.android.synthetic.main.result_details_fragment.*

class ResultDetails: Fragment(R.layout.result_details_fragment) {
    private val navArgs by navArgs<ResultDetailsArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        result_details_text.text = "${navArgs.resultData?.distance}m ${navArgs.resultData?.ss_abbr} ${navArgs.resultData?.res_course_abbr}"
    }
}