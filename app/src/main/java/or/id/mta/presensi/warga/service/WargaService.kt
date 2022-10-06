package or.id.mta.presensi.warga.service

import or.id.mta.presensi.scan.entity.user.UserPresence

interface WargaService {
    fun getWarga(
        token: String,
        name: String,
        officeId: Int,
        onSuccess: (userPresences: List<UserPresence>) -> Unit,
        onError: (message: String) -> Unit
    )
}