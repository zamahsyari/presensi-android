package or.id.mta.presensi.event.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import or.id.mta.presensi.login.service.AddEventService
import or.id.mta.presensi.login.service.EventService

class EventViewModelFactory(val token: LiveData<String>, val eventService: EventService): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventViewModel(eventService = eventService, token = token) as T
    }
}