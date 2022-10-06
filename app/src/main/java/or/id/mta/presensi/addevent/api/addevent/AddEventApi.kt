package or.id.mta.presensi.addevent.api.addevent

import or.id.mta.presensi.addevent.api.majlis.MajlisApiFilter
import or.id.mta.presensi.addevent.entity.addevent.AddEventApiRequest
import or.id.mta.presensi.addevent.entity.majlis.MajlisApiResponse

interface AddEventApi {
    fun addEvent(
        token: String,
        request:AddEventApiRequest,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )
}