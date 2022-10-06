package or.id.mta.presensi.login.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import or.id.mta.presensi.event.entity.EventApiResponse
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.event.entity.EventResponse
import or.id.mta.presensi.login.entity.LoginRequest
import or.id.mta.presensi.login.entity.LoginResponse
import java.time.LocalDate
import java.time.LocalDateTime

class EventConverter {
    companion object{
        fun stringToEventApiResponse(value:String): EventApiResponse{
            val gson = Gson()
            return gson.fromJson(value, EventApiResponse::class.java)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun eventApiResponseToEventEntities(response: EventApiResponse): List<EventEntity>{
            val eventEntities = mutableListOf<EventEntity>()
            response.data.forEach {eventResponse ->
                val eventEntity = EventEntity(
                    id = eventResponse.id,
                    cabang_id = eventResponse.office_id,
                    cabang = eventResponse.office_name,
                    name = eventResponse.name,
                    startAt = LocalDateTime.parse(eventResponse.start_at.removeSuffix(".000Z")),
                    endAt = LocalDateTime.parse(eventResponse.end_at.removeSuffix(".000Z")),
                    isActive = eventResponse.is_active == 1
                )
                eventEntities.add(eventEntity)
            }
            return eventEntities
        }
    }
}