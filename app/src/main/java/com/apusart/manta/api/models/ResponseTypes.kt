package com.apusart.manta.api.models

class AthletesResponse(
    val athletes: List<Athlete>
)

class LastMeetsResponse(
    val lastMeets: List<Meet>
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
    val medalStats: List<MedalStat>
)

class ResultsResponse(
    val results: List<Result>
)