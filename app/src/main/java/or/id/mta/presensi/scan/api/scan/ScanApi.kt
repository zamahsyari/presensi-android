package or.id.mta.presensi.scan.api.scan

import or.id.mta.presensi.scan.entity.presence.PresenceApiEntity
import or.id.mta.presensi.scan.entity.presence.PresenceApiRequest

interface ScanApi {
    fun presence(
        token: String,
        request: PresenceApiRequest,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )
    fun getPresences(
        token: String,
        filter: ScanApiFilter,
        onSuccess: (List<PresenceApiEntity>) -> Unit,
        onError: (message: String) -> Unit
    )
}