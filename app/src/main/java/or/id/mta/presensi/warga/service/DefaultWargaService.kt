package or.id.mta.presensi.warga.service

import android.os.Build
import androidx.annotation.RequiresApi
import or.id.mta.presensi.scan.api.scan.ScanApi
import or.id.mta.presensi.scan.api.scan.ScanApiFilter
import or.id.mta.presensi.scan.api.warga.WargaApi
import or.id.mta.presensi.scan.api.warga.WargaApiFilter
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.statistic.util.StatisticConverter
import or.id.mta.presensi.warga.util.WargaConverter

class DefaultWargaService(val repository: WargaApi): WargaService {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getWarga(
        token: String,
        name: String,
        officeId: Int,
        onSuccess: (userPresences: List<UserPresence>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val filterApi = WargaApiFilter(name, officeId)
        repository.getWargaByName(
            token = token,
            filter = filterApi,
            {response ->
                val presences = WargaConverter.wargaApiEntitiesToUserPresences(response.data)
                onSuccess(presences)
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }
}