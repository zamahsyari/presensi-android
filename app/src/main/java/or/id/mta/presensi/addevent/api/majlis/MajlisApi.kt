package or.id.mta.presensi.addevent.api.majlis

import or.id.mta.presensi.addevent.entity.majlis.MajlisApiResponse

interface MajlisApi {
    fun getPerwakilan(
        token: String,
        filter: MajlisApiFilter,
        onSuccess: (response: MajlisApiResponse) -> Unit,
        onError: (message: String) -> Unit
    )
    fun getCabang(
        token: String,
        filter: MajlisApiFilter,
        perwakilanCode: String,
        onSuccess: (response: MajlisApiResponse) -> Unit,
        onError: (message: String) -> Unit
    )
}