package or.id.mta.presensi.statistic.util

import android.util.Log
import or.id.mta.presensi.scan.entity.presence.PresenceApiEntity
import or.id.mta.presensi.statistic.entity.StatWarga

class StatisticConverter {
    companion object{
        fun presenceApiEntityToStatWarga(presenceApiEntity: PresenceApiEntity): StatWarga{
            var gender = GenderEnum.MALE.value
            if(presenceApiEntity.member_gender == 0){
                gender = GenderEnum.FEMALE.value
            }
            var presence = PresenceStatusEnum.HADIR.value
            if(presenceApiEntity.permit_type == 0){
                presence = PresenceStatusEnum.HADIR.value
            }else if(presenceApiEntity.permit_type == 1){
                presence = PresenceStatusEnum.SAKIT.value
            }else if(presenceApiEntity.permit_type == 2){
                presence = PresenceStatusEnum.KERJA.value
            }else if(presenceApiEntity.permit_type == 3){
                presence = PresenceStatusEnum.PULANG_KAMPUNG.value
            }else{
                presence = PresenceStatusEnum.LAIN_LAIN.value
            }
            return StatWarga(
                name = presenceApiEntity.member_name,
                cabang = presenceApiEntity.office_name,
                gender = gender,
                presence = presence
            )
        }

        fun presenceApiEntitiesToStatWargas(presenceApiEntities: List<PresenceApiEntity>): List<StatWarga>{
            val statWargas = mutableListOf<StatWarga>()
            presenceApiEntities.forEach {presenceApiEntity ->
                statWargas.add(
                    presenceApiEntityToStatWarga(presenceApiEntity)
                )
            }
            return statWargas
        }
    }
}