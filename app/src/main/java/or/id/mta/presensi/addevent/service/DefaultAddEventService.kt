package or.id.mta.presensi.login.service

import or.id.mta.presensi.addevent.api.addevent.AddEventApi
import or.id.mta.presensi.addevent.entity.majlis.MajlisEntity
import or.id.mta.presensi.addevent.api.majlis.MajlisApi
import or.id.mta.presensi.addevent.api.majlis.MajlisApiFilter
import or.id.mta.presensi.addevent.entity.addevent.AddEventEntity
import or.id.mta.presensi.addevent.util.AddEventConverter
import or.id.mta.presensi.login.util.MajlisConverter

class DefaultAddEventService(majlisRepository: MajlisApi, repository: AddEventApi):AddEventService {
    val majlisRepo: MajlisApi = majlisRepository
    val repo: AddEventApi = repository

    override fun getPerwakilan(
        token: String,
        filterName: String?,
        onSuccess: (majlisEntities: List<MajlisEntity>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val filterApi = MajlisApiFilter(filterName)
        majlisRepo.getPerwakilan(
            token,
            filterApi,
            {response ->
                val majlisEntities = MajlisConverter.majlisApiResponseToMajlisEntities(response)
                val sorted = majlisEntities.sortedWith(compareBy({it.name}, {it.name.length}))
                onSuccess(sorted)
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }

    override fun getCabang(
        token: String,
        perwakilanCode:String,
        filterName: String?,
        onSuccess: (majlisEntities: List<MajlisEntity>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val filterApi = MajlisApiFilter(filterName)
        majlisRepo.getCabang(
            token,
            filterApi,
            perwakilanCode,
            {response ->
                val majlisEntities = MajlisConverter.majlisApiResponseToMajlisEntities(response)
                val sorted = majlisEntities.sortedWith(compareBy({it.name}, {it.name.length}))
                onSuccess(sorted)
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }

    override fun addEvent(
        token: String,
        addEventEntity: AddEventEntity,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        repo.addEvent(
            token,
            AddEventConverter.addEventEntityToAddEventApiRequest(addEventEntity),
            {
                onSuccess()
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }
}