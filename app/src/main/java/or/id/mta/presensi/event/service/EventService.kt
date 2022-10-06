package or.id.mta.presensi.login.service

import or.id.mta.presensi.event.entity.EventEntity

interface EventService {
    fun getEvents(
        token: String,
        filterName: String?,
        onSuccess: (eventEntities: List<EventEntity>) -> Unit,
        onError: (message: String) -> Unit
    )
}