package or.id.mta.presensi.event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.login.service.AddEventService
import or.id.mta.presensi.login.service.EventService

class EventViewModel(val token: LiveData<String>, val eventService: EventService): ViewModel() {
    private var _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private var _query = MutableLiveData("")
    val query: LiveData<String> = _query

    private var _eventEntities = MutableLiveData(emptyList<EventEntity>())
    val eventEntities: LiveData<List<EventEntity>> = _eventEntities

    fun fetchData(officeId: LiveData<Int>){
        eventService.getEvents(
            token = token.value!!,
            filterName = query.value,
            filterOfficeId = officeId.value,
            onSuccess = {eventEntities ->
                _eventEntities.postValue(eventEntities)
            },
            onError = {message ->
                _errorMessage.postValue(message)
            }
        )
    }

    fun setQueryAndSearch(officeId: LiveData<Int>, query:String){
        _query.value = query
        fetchData(officeId)
    }
}