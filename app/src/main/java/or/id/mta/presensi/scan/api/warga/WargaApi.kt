package or.id.mta.presensi.scan.api.warga

import or.id.mta.presensi.scan.entity.warga.WargaApiResponse

interface WargaApi {
    fun getWargaBySerialNumber(
        token: String,
        serialNumber: String,
        onSuccess: (response: WargaApiResponse) -> Unit,
        onError: (message: String) -> Unit
    )
    fun getWargaById(
        token: String,
        memberId: Int,
        onSuccess: (response: WargaApiResponse) -> Unit,
        onError: (message: String) -> Unit
    )
    fun getWargaByName(
        token: String,
        filter: WargaApiFilter,
        onSuccess: (response: WargaApiResponse) -> Unit,
        onError: (message: String) -> Unit
    )
}