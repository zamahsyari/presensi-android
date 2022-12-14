package or.id.mta.presensi.event.api

import or.id.mta.presensi.event.entity.EventApiResponse

interface EventApi {
    fun getAll(
        token: String,
        filter: EventApiFilter,
        page: Int,
        onSuccess: (response: EventApiResponse) -> Unit,
        onError: (message: String) -> Unit
    )
}