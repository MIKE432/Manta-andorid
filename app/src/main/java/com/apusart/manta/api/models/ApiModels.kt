package com.apusart.manta.api.models

class Athlete(
    val athlete_id: Int,
    val club_id: Int,
    val ath_from: String?,
    val ath_to: String?,
    val section_id: Int,
    val gender_abbr: String,
    val ath_firstname: String,
    val ath_lastname: String,
    val ath_licence_no: String?,
    val ath_image_min_url: String?,
    val ath_image_max_url: String?,
    val ath_birth_year: Int,
    val ath_birth_date: String,
    val ath_birth_month: Int,
    val ath_birth_day: Int,
    val ath_status: String,
    val ath_email: String?,
    val ath_contact_name: String,
    val ath_contact_email: String,
    val ath_contact_phone: Long,
    val ath_address: String?,
    val cath_code: String,
    val ath_swimrankings_id: Int?
)

class PersonalBest(
    val sst_name_pl:String,
    val sev_distance: Int,
    val sev_sr_style_id: Int,
    val event_id: Int,
    val res_course_abbr: String,
    val res_points: Int,
    val res_split_times: String,
    val res_dsq_abbr: String,
    val res_total_time: String,
    val res_count: Int,
    val mt_from: String,
    val mt_city: String,
    val mt_nation_abbr: String,
    val sr_meet_id: Int?,
    val mt_name: String
)
class MostValuableResult(
    val athlete_id: Int,
    val res_course_abbr: String,
    val event_id: Int,
    val res_total_time: String,
    val res_count: Int,
    val result_id: Int,
    val club_id: Int?,
    val meet_id: Int,
    val res_status: String,
    val res_round_abbr: String,
    val res_entry_time: String,
    val res_place: Int?,
    val res_points: Int,
    val res_prev_best_time: String?,
    val res_feat_abbr: String,
    val res_comment: String,
    val res_split_times: String,
    val res_dsq_abbr: String,
    val res_video_url: String,
    val season_id: Int,
    val gallery_folder_id: Int,
    val mt_status: String,
    val mt_grade_abbr: String,
    val mt_entry_count: Int,
    val mt_best_entry_count: Int,
    val mt_name: String,
    val coach_id: Int,
    val mt_city: String,
    val mt_nation_abbr: String,
    val mt_flag: String,
    val mt_from: String,
    val mt_to: String,
    val mt_entry_deadline: String?,
    val mt_course_abbr: String,
    val sr_meet_id: Int?,
    val mt_place: String?,
    val mt_images_folder:String,
    val mt_main_page: String,
    mt_org_message_page: String,
    val mt_athletes_list_page: String,
    val mt_start_list_page: String,
    val mt_results_page: String,
    val mt_summary_page: String,
    val mt_newspaper_clipping: String,
    val mt_lxf_url_message: String,
    val mt_lxf_url_entries: String,
    val mt_lxf_url_entries_k: String,
    val mt_lxf_url_results: String,
    val style_abbr: String,
    val sev_relay_count: Int,
    val sev_distance: Int,
    val sev_sr_style_id: Int,
    val gender_abbr: String,
    val course_abbr: String,
    val sev_rec: String,
    val sev_WR_SCM_M: String,
    val sev_WR_SCM_M_info: String,
    val sev_WR_SCM_F: String,
    val sev_WR_SCM_F_info: String,
    val sev_WR_LCM_M: String,
    val sev_WR_LCM_M_info: String,
    val sev_WR_LCM_F: String,
    val sev_WR_LCM_F_info: String,
    val sst_abbr_pl: String,
    val sst_name: String,
    val sst_name_pl: String,
    val sst_name_lxf: String
)

class Meet(
    val meet_id: Int,
    val season_id: Int,
    val gallery_folder_id: Int?,
    val mt_status: String,
    val mt_grade_abbr: String,
    val mt_entry_count: Int,
    val mt_best_entry_count: Int,
    val mt_name: String,
    val coach_id: Int,
    val mt_city: String,
    val mt_nation_abbr: String,
    val mt_flag: String,
    val mt_from: String,
    val mt_to: String,
    val mt_entry_deadline: String,
    val mt_course_abbr: String,
    val sr_meet_id: Int?,
    val mt_place: Any,
    val mt_images_folder: String,
    val mt_main_page: String,
    val mt_org_message_page: String,
    val mt_athletes_list_page: String,
    val mt_start_list_page: String,
    val mt_results_page: String,
    val mt_summary_page: String,
    val mt_newspaper_clipping: String,
    val mt_lxf_url_message: String,
    val mt_lxf_url_entries: String,
    val mt_lxf_url_entries_k: String,
    val mt_lxf_url_results: String,
    val results: List<Result>?,
    val photos: List<Photo>?
)

class MedalStat(
    val mt_grade: String,
    val mt_grade_abbr: String,
    val stats: List<MedalDetails>?
)
class MedalDetails(
    val res_place: Int,
    val res_count: Int
)

class Result(
    val result_id: Int,
    val club_id: Int?,
    val meet_id: Int,
    val athlete_id: Int,
    val event_id: Int,
    val res_status: String,
    val res_round_abbr: String,
    val res_entry_time: String?,
    val res_place: Int?,
    val res_total_time: String?,
    val res_points: Int,
    val res_prev_best_time: String?,
    val res_feat_abbr: String,
    val res_comment: String,
    val res_split_times: String,
    val res_dsq_abbr: String,
    val res_course_abbr: String,
    val res_video_url: String,
    val style_abbr: String,
    val sst_abbr_pl: String,
    val sst_name: String,
    val sst_name_pl: String,
    val sst_name_lxf: String,
    val sev_relay_count: Int,
    val sev_distance: Int,
    val sev_sr_style_id: Int,
    val gender_abbr: String,
    val course_abbr: String,
    val sev_rec: String,
    val sev_WR_SCM_M: String,
    val sev_WR_SCM_M_info: String,
    val sev_WR_SCM_F: String,
    val sev_WR_SCM_F_info: String,
    val sev_WR_LCM_M: String,
    val sev_WR_LCM_M_info: String,
    val sev_WR_LCM_F: String,
    val sev_WR_LCM_F_info: String,
    val season_id: Int,
    val gallery_folder_id: Int?,
    val mt_status: String,
    val mt_grade_abbr: String,
    val mt_entry_count: Int,
    val mt_best_entry_count: Int,
    val mt_name: String,
    val coach_id: Int?,
    val mt_city: String,
    val mt_nation_abbr: String,
    val mt_flag: String,
    val mt_from: String,
    val mt_to: String,
    val mt_entry_deadline: String?,
    val mt_course_abbr: String,
    val sr_meet_id: Int,
    val mt_place: Int,
    val mt_images_folder: String,
    val mt_main_page: String,
    val mt_org_message_page: String,
    val mt_athletes_list_page: String,
    val mt_start_list_page: String,
    val mt_results_page: String,
    val mt_summary_page: String,
    val mt_newspaper_clipping: String,
    val mt_lxf_url_message: String,
    val mt_lxf_url_entries: String,
    val mt_lxf_url_entries_k: String,
    val mt_lxf_url_results: String
)

class Record(
val rsb_id: Int,
    val club_id: Int,
    val athlete_id: Int,
    val rsb_athlete: String,
    val gender_abbr: String,
    val event_id: Int,
    val course_abbr: String,
    val meet_id: Int,
    val rsb_meet_city: String,
    val rsb_meet_date: String,
    val rsb_age: Int,
    val rsb_time: String,
    val rsb_feat_abbr: String,
    val rsb_status: String,
    val style_abbr: String,
    val sst_abbr_pl: String,
    val sst_name: String,
    val sst_name_pl: String,
    val sst_name_lxf: String,
    val sev_relay_count: Int,
    val sev_distance: Int,
    val sev_sr_style_id: Int,
    val sev_rec: String,
    val sev_WR_SCM_M: String,
    val sev_WR_SCM_M_info: String,
    val sev_WR_SCM_F_info: String,
    val sev_WR_LCM_M: String,
    val sev_WR_LCM_M_info: String,
    val sev_WR_LCM_F: String,
    val sev_WR_LCM_F_info: String,
    val season_id: Int,
    val gallery_folder_id: Int,
    val mt_status: String,
    val mt_grade_abbr: String,
    val mt_entry_count: Int,
    val mt_best_entry_count: Int,
    val mt_name: String,
    val coach_id: Int,
    val mt_city: String,
    val mt_nation_abbr: String,
    val mt_flag: String,
    val mt_from: String,
    val mt_to: String,
    val mt_entry_deadline: String?,
    val mt_course_abbr: String,
    val sr_meet_id: String?,
    val mt_place: String?,
    val mt_images_folder: String,
    val mt_main_page: String,
    val mt_org_message_page: String,
    val mt_athletes_list_page: String,
    val mt_start_list_page: String,
    val mt_results_page: String,
    val mt_summary_page: String,
    val mt_newspaper_clipping: String,
    val mt_lxf_url_message: String,
    val mt_lxf_url_entries: String,
    val mt_lxf_url_entries_k: String,
    val mt_lxf_url_results: String
)

class Photo(
    val path: String,
    val path_thumb_m: String?,
    val path_thumb_s: String?
)