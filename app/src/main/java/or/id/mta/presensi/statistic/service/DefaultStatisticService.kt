package or.id.mta.presensi.statistic.service

import or.id.mta.presensi.event.api.EventApiFilter
import or.id.mta.presensi.scan.api.scan.ScanApi
import or.id.mta.presensi.scan.api.scan.ScanApiFilter
import or.id.mta.presensi.scan.util.ScanConverter
import or.id.mta.presensi.statistic.entity.StatWarga
import or.id.mta.presensi.statistic.util.StatisticConverter

class DefaultStatisticService(val repository: ScanApi): StatisticService {
    override fun getPresences(
        token: String,
        eventId: Int,
        onSuccess: (presences: List<StatWarga>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val filterApi = ScanApiFilter(eventId)
        repository.getPresences(
            token = token,
            filter = filterApi,
            {response ->
                val presences = StatisticConverter.presenceApiEntitiesToStatWargas(response)
                onSuccess(presences)
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }
}