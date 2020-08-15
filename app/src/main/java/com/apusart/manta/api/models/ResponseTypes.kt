package com.apusart.manta.api.models

class AthletesResponse(
    val athletes: List<Athlete>
)

class MeetsResponse(
    val meets: List<Meet>
)

class IncomingMeetsResponse(
    val IncomingMeets: List<Meet>
)

class PersonalBestsResponse(
    val personalBestResults: List<PersonalBest>
)

class MostValuableResultsResponse(
    val mostValuableResults: List<MostValuableResult>
)
class MedalStatsResponse(
    val meetGrade: MedalStat
)

class ResultsResponse(
    val results: List<Result>
)

class RecordsResponse(
    val records: List<Record>
)

class MeetDetailsResponse(
    val meet: Meet
)