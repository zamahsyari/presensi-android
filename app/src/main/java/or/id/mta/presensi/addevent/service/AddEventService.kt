package or.id.mta.presensi.login.service

import or.id.mta.presensi.addevent.entity.addevent.AddEventEntity
import or.id.mta.presensi.addevent.entity.majlis.MajlisEntity

interface AddEventService {
    fun getPerwakilan(
        token: String,
        filterName: String?,
        onSuccess: (majlisEntities: List<MajlisEntity>) -> Unit,
        onError: (message: String) -> Unit
    )
    fun getCabang(
        token: String,
        perwakilanCode:String,
        filterName: String?,
        onSuccess: (majlisEntities: List<MajlisEntity>) -> Unit,
        onError: (message: String) -> Unit
    )
    fun addEvent(
        token: String,
        addEventEntity: AddEventEntity,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )
}