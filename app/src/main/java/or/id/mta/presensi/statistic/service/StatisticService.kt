package or.id.mta.presensi.statistic.service

import or.id.mta.presensi.statistic.entity.StatWarga

interface StatisticService {
    fun getPresences(
        token: String,
        eventId: Int,
        onSuccess: (presences: List<StatWarga>) -> Unit,
        onError: (message: String) -> Unit
    )
}