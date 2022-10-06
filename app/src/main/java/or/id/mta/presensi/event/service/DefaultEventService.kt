package or.id.mta.presensi.login.service

import android.os.Build
import androidx.annotation.RequiresApi
import or.id.mta.presensi.event.api.EventApi
import or.id.mta.presensi.event.api.EventApiFilter
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.login.util.EventConverter

class DefaultEventService(repository: EventApi):EventService {
    val repo:EventApi = repository

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getEvents(token:String, filterName:String?, onSuccess: (eventEntities: List<EventEntity>) -> Unit, onError: (message: String) -> Unit) {
        val filterApi = EventApiFilter(filterName)
        repo.getAll(
            token,
            filterApi,
            {response ->
                onSuccess(EventConverter.eventApiResponseToEventEntities(response))
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }
}