package or.id.mta.presensi.login.service

import or.id.mta.presensi.scan.entity.presence.PresenceEntity
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.statistic.entity.StatWarga

interface ScanService {
    fun presence(
        token: String,
        presenceEntity: PresenceEntity,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )
    fun getWarga(
        token: String,
        memberId: Int,
        serialNumber: String,
        onSuccess: (userPresence: UserPresence) -> Unit,
        onError: (message: String) -> Unit
    )
    fun getPresences(
        token: String,
        eventId: Int,
        onSuccess: (presences: List<StatWarga>) -> Unit,
        onError: (message: String) -> Unit
    )
}