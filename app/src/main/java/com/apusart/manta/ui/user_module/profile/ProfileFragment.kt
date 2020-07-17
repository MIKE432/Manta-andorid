package com.apusart.manta.ui.user_module.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.apusart.manta.R
import com.apusart.manta.api.models.Athlete
import com.apusart.manta.ui.tools.Prefs
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment: Fragment(R.layout.profile_fragment) {
    private var mUser: Athlete? = Prefs.getUser()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mUser != null) {

            if (mUser!!.ath_licence_no != "") {
                profile_fragment_athlete_licence_title.text = getString(R.string.licence_number)
                profile_fragment_athlete_licence_value.text = mUser!!.ath_licence_no
            } else {
                profile_fragment_athlete_licence_container.isVisible = false
            }

            profile_fragment_athlete_birth_date_title.text = getString(R.string.birth_date)

            if(mUser!!.ath_birth_day == 0)
                profile_fragment_athlete_birth_date_value.text = mUser?.ath_birth_year.toString()
            else {
                profile_fragment_athlete_birth_date_value.text = mUser?.ath_birth_date
            }

            if(mUser!!.ath_from != null) {
                profile_fragment_athlete_train_from_title.text =
                    if (mUser!!.ath_to == null)
                        getString(R.string.train_from)
                    else (if (mUser!!.gender_abbr == "M")
                        getString(R.string.train_from_m)
                    else getString(R.string.train_from_f)
                            )

                profile_fragment_athlete_train_from_value.text = mUser?.ath_from
            } else {
                profile_fragment_athlete_train_from_container.isVisible = false
            }

            if(mUser!!.ath_to != null) {
                profile_fragment_athlete_train_to_title.text = if (mUser!!.gender_abbr == "M") getString(R.string.train_to_m) else getString(R.string.train_to_f)
                profile_fragment_athlete_train_to_value.text = mUser?.ath_to
            } else {
                profile_fragment_athlete_train_to_container.isVisible = false
            }
        }
    }
}