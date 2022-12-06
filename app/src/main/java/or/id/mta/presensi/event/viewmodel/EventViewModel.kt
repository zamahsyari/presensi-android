package or.id.mta.presensi.event.viewmodel

import android.util.Log
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

    private var _page = MutableLiveData(1)
    val page: LiveData<Int> = _page

    fun fetchData(officeId: LiveData<Int>){
        _page.postValue(1)
        eventService.getEvents(
            token = token.value!!,
            filterName = query.value,
            filterOfficeId = officeId.value,
            page = page.value!!,
            onSuccess = {eventEntities ->
                _eventEntities.postValue(eventEntities)
            },
            onError = {message ->
                _errorMessage.postValue(message)
            }
        )
    }

    fun fetchDataNext(officeId: LiveData<Int>){
        _page.postValue(page.value!! + 1)
        if(page.value!! > 1){
            eventService.getEvents(
                token = token.value!!,
                filterName = query.value,
                filterOfficeId = officeId.value,
                page = page.value!!,
                onSuccess = {eventEntities ->
                    var added: List<EventEntity> = mutableListOf()
                    added = _eventEntities.value!! + eventEntities
                    _eventEntities.postValue(added)
                },
                onError = {message ->
                    _errorMessage.postValue(message)
                }
            )
        }
    }

    fun setQueryAndSearch(officeId: LiveData<Int>, query:String){
        _query.value = query
        fetchData(officeId)
    }
}