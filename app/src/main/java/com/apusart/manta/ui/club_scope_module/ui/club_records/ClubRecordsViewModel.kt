package com.apusart.manta.ui.club_scope_module.ui.club_records

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Record
import com.apusart.manta.api.serivces.MantaService
import com.apusart.manta.ui.tools.Const
import com.apusart.manta.ui.tools.RecordListItemData
import kotlinx.coroutines.launch
import java.lang.Exception

class ClubRecordsViewModel(private val lifecycleOwner: LifecycleOwner): ViewModel() {
    private val mantaService = MantaService()
    val ageSpinnerValue: MutableLiveData<Int>
    val isFemale: MutableLiveData<Boolean>
    val isShortCourse: MutableLiveData<Boolean>
    val recordsData = MutableLiveData<List<RecordListItemData>>()
    var howMuchToIgnore: Int = 0

    //filters
    val ageSpinnerValueF = MutableLiveData<Int>(0)
    val isFemaleF = MutableLiveData<Boolean>(true)
    val isShortCourseF = MutableLiveData<Boolean>(true)

    init {

        ageSpinnerValue = MutableLiveData<Int>(0)
        isFemale = MutableLiveData(true)
        isShortCourse = MutableLiveData(true)
        getRecords()
    }

    fun getGenderStringFilter(): String {
        return if(isFemale.value != false) "Kobiety" else "Mężczyźni"
    }

    fun getAgeStringFilter(): String {
        return when(ageSpinnerValue.value) {
            0 -> "10 lat i młodsi"
            9 -> "19 lat i starsi"
            else -> "${ageSpinnerValue.value?.plus(10)} lat"
        }
    }

    fun commitFilters() {
        ageSpinnerValue.value = ageSpinnerValueF.value
        isFemale.value = isFemaleF.value
        isShortCourse.value = isShortCourseF.value

    }

    fun rejectFilters() {
        ageSpinnerValueF.value = ageSpinnerValue.value
        isFemaleF.value = isFemale.value
        isShortCourseF.value = isShortCourse.value
    }

    fun getCourseStringFilter(): String {
        return if(isShortCourse.value != false) "25m" else "50m"
    }

    fun getRecords() {
        viewModelScope.launch {
            try {
                howMuchToIgnore++

                val result = mantaService.getRecords(ageSpinnerValue.value?.plus(10), course = Const.courseFilter.getString(isShortCourse.value.toString()), gender = if(isFemale.value != false) "F" else "M")

                if(--howMuchToIgnore == 0) {
                    val parsedResult = ArrayList<RecordListItemData>()

                    val freeStyleList = result.filter { it.style_abbr == "FR" }
                    val backStrokeStyleList = result.filter { it.style_abbr == "BK" }
                    val flyStyleList = result.filter { it.style_abbr == "FL" }
                    val breastStrokeStyleList = result.filter { it.style_abbr == "BT" }
                    val medleyStyleList = result.filter { it.style_abbr == "ME" }

                    parsedResult.add(RecordListItemData("Dowolny",
                        freeStyleList.firstOrNull { it.sev_distance == 50 },
                        freeStyleList.firstOrNull { it.sev_distance == 100 },
                        freeStyleList.firstOrNull { it.sev_distance == 200 },
                        freeStyleList.firstOrNull { it.sev_distance == 400 },
                        freeStyleList.firstOrNull { it.sev_distance == 800 },
                        freeStyleList.firstOrNull { it.sev_distance == 1500 }
                    ))

                    parsedResult.add(RecordListItemData("Grzbietowy",
                        backStrokeStyleList.firstOrNull { it.sev_distance == 50 },
                        backStrokeStyleList.firstOrNull { it.sev_distance == 100 },
                        backStrokeStyleList.firstOrNull { it.sev_distance == 200 },
                        null,
                        null,
                        null
                    ))

                    parsedResult.add(RecordListItemData("Motylkowy",
                        flyStyleList.firstOrNull { it.sev_distance == 50 },
                        flyStyleList.firstOrNull { it.sev_distance == 100 },
                        flyStyleList.firstOrNull { it.sev_distance == 200 },
                        null,
                        null,
                        null
                    ))

                    parsedResult.add(RecordListItemData("Klasyczny",
                        breastStrokeStyleList.firstOrNull { it.sev_distance == 50 },
                        breastStrokeStyleList.firstOrNull { it.sev_distance == 100 },
                        breastStrokeStyleList.firstOrNull { it.sev_distance == 200 },
                        null,
                        null,
                        null
                    ))


                    parsedResult.add(RecordListItemData("Zmienny",
                        medleyStyleList.firstOrNull { it.sev_distance == 50 },
                        medleyStyleList.firstOrNull { it.sev_distance == 100 },
                        medleyStyleList.firstOrNull { it.sev_distance == 200 },
                        null,
                        null,
                        null
                    ))


                    recordsData.value = parsedResult
                }
            } catch(e: Exception) {
                e.printStackTrace()
            } finally {

            }

        }
    }
}

@BindingAdapter("clubRecordsAgeValue")
fun setAgeValue(spinner: Spinner, value: Int?) {

    if(value == null)
        return

    spinner.setSelection(value)
}

@InverseBindingAdapter(attribute = "clubRecordsAgeValue", event = "clubRecordsAgeValueAttrChanged")
fun getAgeValue(spinner: Spinner): Int {

    return spinner.selectedItemPosition
}

@BindingAdapter("clubRecordsAgeValueAttrChanged")
fun onAgeValueChangedAdapter(spinner: Spinner, clubRecordsAgeValueAttrChanged: InverseBindingListener?) {

    val newValue: AdapterView.OnItemSelectedListener? = if(clubRecordsAgeValueAttrChanged == null)
        null
    else
        object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                clubRecordsAgeValueAttrChanged.onChange()
            }
        }

    spinner.onItemSelectedListener = newValue
}

//
//@BindingAdapter("clubRecordsGenderValue")
//fun setGenderValue(spinner: Spinner, value: Int?) {
//
//    if(value == null)
//        return
//
//    spinner.setSelection(value)
//}
//
//@InverseBindingAdapter(attribute = "clubRecordsGenderValue", event = "clubRecordsGenderValueAttrChanged")
//fun getGenderValue(spinner: Spinner): Int {
//
//    return spinner.selectedItemPosition
//}
//
//@BindingAdapter("clubRecordsGenderValueAttrChanged")
//fun onGenderValueChangedAdapter(spinner: Spinner, clubRecordsGenderValueAttrChanged: InverseBindingListener?) {
//
//    val newValue: AdapterView.OnItemSelectedListener? = if(clubRecordsGenderValueAttrChanged == null)
//        null
//    else
//        object: AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                clubRecordsGenderValueAttrChanged.onChange()
//            }
//        }
//
//    spinner.onItemSelectedListener = newValue
//}
//
//@BindingAdapter("clubRecordsCourseValue")
//fun setCourseValue(spinner: Spinner, value: Int?) {
//    if(value == null)
//        return
//
//    spinner.setSelection(value)
//}
//
//@InverseBindingAdapter(attribute = "clubRecordsCourseValue", event = "clubRecordsCourseValueAttrChanged")
//fun getCourseValue(spinner: Spinner): Int {
//    return spinner.selectedItemPosition
//}
//
//@BindingAdapter("clubRecordsCourseValueAttrChanged")
//fun onCourseValueChangedAdapter(spinner: Spinner, clubRecordsCourseValueAttrChanged: InverseBindingListener?) {
//
//    val newValue: AdapterView.OnItemSelectedListener? = if(clubRecordsCourseValueAttrChanged == null)
//        null
//    else
//        object: AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                clubRecordsCourseValueAttrChanged.onChange()
//            }
//        }
//
//    spinner.onItemSelectedListener = newValue
//}