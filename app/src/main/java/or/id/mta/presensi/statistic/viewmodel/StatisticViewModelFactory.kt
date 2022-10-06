package or.id.mta.presensi.statistic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import or.id.mta.presensi.statistic.service.StatisticService

class StatisticViewModelFactory(
    val token: LiveData<String>,
    val selectedEventId: LiveData<Int>,
    val statisticService: StatisticService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatisticViewModel(token, selectedEventId, statisticService) as T
    }
}