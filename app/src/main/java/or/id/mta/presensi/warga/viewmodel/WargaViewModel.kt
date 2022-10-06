package or.id.mta.presensi.warga.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.login.service.EventService
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.warga.service.WargaService

class WargaViewModel(
    val token: LiveData<String>,
    val event: LiveData<EventEntity>,
    val wargaService: WargaService
    ):ViewModel() {
    private var _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private var _query = MutableLiveData("")
    val query: LiveData<String> = _query

    private var _eventEntities = MutableLiveData(emptyList<UserPresence>())
    val eventEntities: LiveData<List<UserPresence>> = _eventEntities

    fun fetchData(){
        wargaService.getWarga(
            token = token.value!!,
            name = query.value!!,
            officeId = event.value!!.cabang_id,
            onSuccess = {userPresences ->
                _eventEntities.postValue(userPresences)
            },
            onError = {message ->
                _errorMessage.postValue(message)
            }
        )
    }

    fun setQueryAndSearch(query:String){
        _query.value = query
        fetchData()
    }
}