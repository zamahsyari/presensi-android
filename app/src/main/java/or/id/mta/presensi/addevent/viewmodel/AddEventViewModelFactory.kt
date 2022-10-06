package or.id.mta.presensi.addevent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import or.id.mta.presensi.login.service.AddEventService
import or.id.mta.presensi.login.service.EventService

class AddEventViewModelFactory(token: LiveData<String>, addEventService: AddEventService): ViewModelProvider.Factory {
    val token = token
    val addEventService = addEventService
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEventViewModel(token = token, addEventService = addEventService) as T
    }
}