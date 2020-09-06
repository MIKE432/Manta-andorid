package com.apusart.manta.ui.user_module.meets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.R
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.Prefs
import com.apusart.manta.ui.tools.Tools
import kotlinx.android.synthetic.main.meet_fragment.*
import kotlinx.android.synthetic.main.result_comparison_item.view.*
import java.math.RoundingMode
import java.text.DecimalFormat


class MeetFragment: Fragment(R.layout.meet_fragment) {
    private val mMeetViewModel: MeetViewModel by viewModels()
    private lateinit var mComparedResultAdapter: ComparedResultAdapter
    private val mArgs by navArgs<MeetFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mComparedResultAdapter = ComparedResultAdapter()

        last_meet_fragment_no_last_meet.isVisible = false
        last_meet_fragment_results.apply {
            adapter = mComparedResultAdapter
        }

        multiple_button.setText(0, "WWW")
        multiple_button.setButtonIcon(0, R.drawable.www_icon)
        multiple_button.setText(1, "Lista startowa")
        multiple_button.setButtonIcon(1, R.drawable.articles_icon)
        multiple_button.addButton("Wyniki")
        multiple_button.setButtonIcon(2, R.drawable.stopwatch_icon)
        multiple_button.addButton("Galeria")
        multiple_button.setButtonIcon(3, R.drawable.gallery_icon64)

        mMeetViewModel.inProgress.observe(viewLifecycleOwner, Observer {

            last_meet_fragment_spinner.isVisible = it
//            last_meet_fragment_nested_scroll_view.isVisible = !it
        })

        mMeetViewModel.mShowContent.observe(viewLifecycleOwner, Observer {

//            last_meet_fragment_nested_scroll_view.isVisible = it
            last_meet_fragment_no_last_meet.isVisible = !it
        })

        mMeetViewModel.meetPhotos.observe(viewLifecycleOwner, Observer {
            val areThereAnyPhotos = it.isNotEmpty() ?: false

            if(areThereAnyPhotos) {
                multiple_button.setButtonOnClickListener(3) {
                    if(mArgs.meetId != -1) {
                        Prefs.setPreviousMeetPhoto(0)
                        findNavController().navigate(MeetFragmentDirections.actionMeetFragmentToGalleryFragment(mArgs.meetId))
                    }

                }
            } else {
                multiple_button.setToInactive(3, R.color.pale_grey_three)
            }
        })

        mMeetViewModel.meet.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                last_meet_fragment_header.text = it.mt_name
                last_meet_fragment_course.text = Const.courseSize.getString(it.mt_course_abbr)
                last_meet_fragment_date.text = resources.getString(R.string.meeting_date, it.mt_from, it.mt_to)
                last_meet_fragment_place.text = it.mt_city
                last_meet_fragment_course.text = Const.courseSize.getString(it.mt_course_abbr)

                if(it.mt_main_page != "") {

                    multiple_button.setButtonOnClickListener(0) { v ->

                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(it.mt_main_page))
                        startActivity(intent)
                    }
                } else {
                    multiple_button.setToInactive(0, R.color.pale_grey_three)
                }

                if(it.mt_start_list_page != "") {

                    multiple_button.setButtonOnClickListener(1) { v ->
                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(it.mt_start_list_page))
                        startActivity(intent)
                    }
                } else {
                    multiple_button.setToInactive(1, R.color.pale_grey_three)
                }

                if(it.mt_results_page != "") {

                    multiple_button.setButtonOnClickListener(2) { v ->
                        val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(it.mt_results_page))
                        startActivity(intent)
                    }
                } else {
                    multiple_button.setToInactive(2, R.color.pale_grey_three)

                }
            } else {
//                last_meet_fragment_nested_scroll_view.isVisible = false
                last_meet_fragment_no_last_meet.isVisible = true
            }


        })

        mMeetViewModel.meetResultCompared.observe(viewLifecycleOwner, Observer {
            mComparedResultAdapter.submitList(it)
        })

        mMeetViewModel.getResultsFromMeetByAthleteId(Prefs.getUser()!!, mArgs.meetId)
    }
}

class ComparedResultAdapter: ListAdapter<ComparedResult, ComparedResultViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<ComparedResult>() {
        override fun areItemsTheSame(oldItem: ComparedResult, newItem: ComparedResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ComparedResult, newItem: ComparedResult): Boolean {
            return oldItem.result.result_id == newItem.result.result_id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComparedResultViewHolder {
        val container = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_comparison_item, parent, false)

        return ComparedResultViewHolder(container)
    }

    override fun onBindViewHolder(holder: ComparedResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ComparedResultViewHolder(container: View): RecyclerView.ViewHolder(container) {
    fun bind(item: ComparedResult) {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_DOWN
        val src = when(item.result.res_place) {
            1 -> R.drawable.medal_gold_icon32
            2 -> R.drawable.medal_silver_icon32
            3 -> R.drawable.medal_bronze_icon32
            else -> 0
        }

        itemView.result_comparison_item_split_times_container.isVisible = item.result.res_split_times != ""
        val x = (item.result.res_place != null && item.result.res_dsq_abbr == "")

        itemView.medalIcon.visibility = if(x) View.VISIBLE else View.INVISIBLE
        itemView.place.isVisible = x
        itemView.result_comparison_item_dsq.isVisible = item.result.res_dsq_abbr != ""
        itemView.result_comparison_item_actual_points.isVisible = item.result.res_points != 0

        if(item.result.res_dsq_abbr != "") {
            itemView.result_comparison_item_actual_time.setTextColor(itemView.resources.getColor(R.color.cool_grey))
        }

        itemView.apply {
            result_comparison_item_header.text = resources.getString(R.string.concurence_no_course, item.result.sev_distance.toString(), item.result.sst_name_pl)
//            result_comparison_item_progress_value.text = df.format(item.progress* 100).toString() + "%"
            medalIcon.setBackgroundResource(src)
            place.text = itemView.resources.getString(R.string.place, item.result.res_place.toString())
            result_comparison_item_club_record_header.text = resources.getString(R.string.club_record, item.record?.rsb_age.toString() ?: "--")
            result_comparison_item_club_record.text = Tools.convertResult(item.record?.rsb_time?.toFloat())
            result_comparison_item_entry_time.text = Tools.convertResult(item.result.res_entry_time?.toFloat())
            result_comparison_item_best_prev_time.text = Tools.convertResult(item.result.res_prev_best_time?.toFloat())
            result_comparison_item_actual_time.text = Tools.convertResult(item.result.res_total_time?.toFloat())
            result_comparison_item_actual_points.text = item.result.res_points.toString()
            result_comparison_item_additional_info_split_times.text = item.result.res_split_times
            result_comparison_item_dsq.text = item.result.res_dsq_abbr
        }
    }
}
