package or.id.mta.presensi.scan.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import or.id.mta.presensi.scan.entity.presence.PresenceApiRequest
import or.id.mta.presensi.scan.entity.presence.PresenceApiResponse
import or.id.mta.presensi.scan.entity.presence.PresenceEntity
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.scan.entity.warga.WargaApiEntity
import or.id.mta.presensi.scan.entity.warga.WargaApiResponse
import or.id.mta.presensi.statistic.util.GenderEnum
import java.time.LocalDate
import java.time.LocalDateTime

class ScanConverter {
    companion object{
        fun presenceEntityToPresenceApiRequest(presenceEntity: PresenceEntity): PresenceApiRequest {
            var note = ""
            var permitType = 0
            Log.d("PRESENCE ENTITY", presenceEntity.toString())
            if(presenceEntity.presenceType.equals("hadir".uppercase())){
                permitType = 0
            }else if(presenceEntity.presenceType.equals("sakit".uppercase())){
                permitType = 1
            }else if(presenceEntity.presenceType.equals("izin".uppercase())){
                if(presenceEntity.permitDetail.equals("kerja".uppercase())){
                    permitType = 2
                }else if(presenceEntity.permitDetail.equals("pulang kampung".uppercase())){
                    permitType = 3
                }else{
                    permitType = 4
                    note = presenceEntity.note
                }
            }
            return PresenceApiRequest(
                member_id = presenceEntity.memberId,
                event_id = presenceEntity.eventId,
                note = note,
                permit_type = permitType
            )
        }

        fun presenceApiRequestToRequestBody(request: PresenceApiRequest): RequestBody {
            val gson = Gson()
            return RequestBody.create(MediaType.get("application/json"), gson.toJson(request))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun wargaApiEntityToUserPresence(wargaApiEntity: WargaApiEntity): UserPresence {
            val today = LocalDate.now()
            var age = 0
            if(wargaApiEntity.member_birthday != "0000-00-00"){
                val birthday = LocalDateTime.parse(wargaApiEntity.member_birthday
                    .removeSuffix(".000Z")
                )
                age = today.year - birthday.year
            }
            var gender = GenderEnum.MALE.value
            if(wargaApiEntity.member_gender == 0){
                gender = GenderEnum.FEMALE.value
            }
            return UserPresence(
                id = wargaApiEntity.member_id,
                name = wargaApiEntity.member_name,
                cabang = wargaApiEntity.office_name,
                age = age,
                gender = gender
            )
        }

        fun stringToWargaApiResponse(value:String): WargaApiResponse {
            val gson = Gson()
            return gson.fromJson(value, WargaApiResponse::class.java)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun wargaApiResponseToUserPresence(response: WargaApiResponse): UserPresence?{
            response.data.forEach {wargaApiEntity ->
                return wargaApiEntityToUserPresence(wargaApiEntity)
            }
            return null
        }

        fun stringToPresenceApiResponse(value:String): PresenceApiResponse {
            val gson = Gson()
            return gson.fromJson(value, PresenceApiResponse::class.java)
        }
    }
}