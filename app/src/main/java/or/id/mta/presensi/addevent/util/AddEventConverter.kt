package or.id.mta.presensi.addevent.util

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import or.id.mta.presensi.addevent.entity.addevent.AddEventApiRequest
import or.id.mta.presensi.addevent.entity.addevent.AddEventEntity
import or.id.mta.presensi.login.entity.LoginRequest

class AddEventConverter {
    companion object{
        fun addEventApiRequestToRequestBody(request: AddEventApiRequest): RequestBody {
            val gson = Gson()
            return RequestBody.create(MediaType.get("application/json"), gson.toJson(request))
        }
        fun addEventEntityToAddEventApiRequest(addEventEntity: AddEventEntity): AddEventApiRequest{
            return AddEventApiRequest(
                office_id = addEventEntity.officeId,
                name = addEventEntity.name,
                start_at = "${addEventEntity.startDate} ${addEventEntity.startTime}:00",
                end_at = "${addEventEntity.startDate} ${addEventEntity.endTime}:00",
                is_active = addEventEntity.isActive,
                description = addEventEntity.description,
                interval = addEventEntity.interval
            )
        }
    }
}