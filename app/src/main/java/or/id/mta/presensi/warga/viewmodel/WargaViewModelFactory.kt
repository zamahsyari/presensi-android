package or.id.mta.presensi.warga.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.event.viewmodel.EventViewModel
import or.id.mta.presensi.login.service.EventService
import or.id.mta.presensi.warga.service.WargaService

class WargaViewModelFactory(
    val token: LiveData<String>,
    val event: LiveData<EventEntity>,
    val wargaService: WargaService): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WargaViewModel(wargaService = wargaService, token = token, event = event) as T
    }
}