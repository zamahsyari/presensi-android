package or.id.mta.presensi.scan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.login.service.AddEventService
import or.id.mta.presensi.login.service.ScanService

class ScanViewModelFactory(
    val token: LiveData<String>,
    val serialNumber: LiveData<String>,
    val memberId: LiveData<Int>,
    val event: LiveData<EventEntity>,
    val scanService: ScanService
):ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScanViewModel(
            token = token,
            scanService = scanService,
            serialNumber = serialNumber,
            event = event,
            memberId = memberId) as T
    }
}