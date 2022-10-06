package or.id.mta.presensi.scan.service

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import or.id.mta.presensi.login.service.ScanService
import or.id.mta.presensi.scan.api.scan.ScanApi
import or.id.mta.presensi.scan.api.scan.ScanApiFilter
import or.id.mta.presensi.scan.api.warga.WargaApi
import or.id.mta.presensi.scan.entity.presence.PresenceEntity
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.scan.util.ScanConverter
import or.id.mta.presensi.statistic.entity.StatWarga
import or.id.mta.presensi.statistic.util.StatisticConverter

class DefaultScanService(val repository: ScanApi, val wargaRepository: WargaApi): ScanService {

    override fun presence(
        token: String,
        presenceEntity: PresenceEntity,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        repository.presence(
            token,
            ScanConverter.presenceEntityToPresenceApiRequest(presenceEntity),
            {
                onSuccess()
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getWarga(
        token: String,
        memberId: Int,
        serialNumber: String,
        onSuccess: (userPresence: UserPresence) -> Unit,
        onError: (message: String) -> Unit
    ) {
        if(memberId != 0){
            wargaRepository.getWargaById(
                token,
                memberId,
                {response ->
                    val userPresence = ScanConverter.wargaApiResponseToUserPresence(response)
                    if (userPresence != null) {
                        onSuccess(userPresence)
                    }else{
                        onError("404")
                    }
                },
                {errorMessage ->
                    onError(errorMessage)
                }
            )
        }
        if(serialNumber != ""){
            wargaRepository.getWargaBySerialNumber(
                token,
                serialNumber,
                {response ->
                    val userPresence = ScanConverter.wargaApiResponseToUserPresence(response)
                    if (userPresence != null) {
                        onSuccess(userPresence)
                    }else{
                        onError("404")
                    }
                },
                {errorMessage ->
                    onError(errorMessage)
                }
            )
        }
    }

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
                try{
                    val presences = StatisticConverter.presenceApiEntitiesToStatWargas(response)
                    onSuccess(presences)
                }catch (e:Exception){
                    onError(e.message.toString())
                }
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }
}