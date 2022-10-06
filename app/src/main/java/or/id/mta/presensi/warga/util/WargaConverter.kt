package or.id.mta.presensi.warga.util

import android.os.Build
import androidx.annotation.RequiresApi
import or.id.mta.presensi.scan.entity.presence.PresenceApiEntity
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.scan.entity.warga.WargaApiEntity
import or.id.mta.presensi.statistic.entity.StatWarga
import or.id.mta.presensi.statistic.util.GenderEnum
import or.id.mta.presensi.statistic.util.PresenceStatusEnum
import java.time.LocalDate
import java.time.LocalDateTime

class WargaConverter {
    companion object{
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
                age = 0,
                gender = ""
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun wargaApiEntitiesToUserPresences(wargaApiEntities: List<WargaApiEntity>): List<UserPresence>{
            val userPresences = mutableListOf<UserPresence>()
            wargaApiEntities.forEach {wargaApiEntity ->
                userPresences.add(WargaConverter.wargaApiEntityToUserPresence(wargaApiEntity))
            }
            return userPresences
        }
    }
}