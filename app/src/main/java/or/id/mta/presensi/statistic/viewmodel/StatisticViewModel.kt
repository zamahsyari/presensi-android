package or.id.mta.presensi.statistic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import or.id.mta.presensi.common.StatisticExtension
import or.id.mta.presensi.statistic.entity.StatWarga
import or.id.mta.presensi.statistic.service.StatisticService
import or.id.mta.presensi.statistic.util.GenderEnum
import or.id.mta.presensi.statistic.util.PresenceStatusEnum

class StatisticViewModel(
    val token: LiveData<String>,
    val selectedEventId: LiveData<Int>,
    val statisticService: StatisticService): ViewModel() {

    private var _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private var _listWarga = MutableLiveData<List<StatWarga>>()
    val listWarga: LiveData<List<StatWarga>> = _listWarga

    private var _cabang = MutableLiveData("")
    val cabang: LiveData<String> = _cabang

    private var _groupedListWarga = MutableLiveData<List<List<StatWarga>>>()
    val groupedListWarga: LiveData<List<List<StatWarga>>> = _groupedListWarga

    private var _totalMale = MutableLiveData(0)
    val totalMale: LiveData<Int> = _totalMale

    private var _totalFemale = MutableLiveData(0)
    val totalFemale: LiveData<Int> = _totalFemale

    fun generateListWarga(): List<StatWarga>{
        val listWarga = mutableListOf<StatWarga>()
        listWarga.add(StatWarga(name = "Wawan Abdul Muin", cabang = "Cabang Gemolong", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Budi", cabang = "Cabang Gemolong 2", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Setiawan", cabang = "Cabang Gemolong", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Hartanto", cabang = "Cabang Gemolong 2", gender = "L"))
        listWarga.add(StatWarga(name = "Siti Maemunah", cabang = "Cabang Gemolong", gender = "P"))
        listWarga.add(StatWarga(name = "Zahrotun", cabang = "Cabang Gemolong 2", gender = "P"))
        listWarga.add(StatWarga(name = "Setyowati", cabang = "Cabang Gemolong", gender = "P"))
        listWarga.add(StatWarga(name = "Sri Ningsih", cabang = "Cabang Gemolong 2", gender = "P"))

        listWarga.add(StatWarga(name = "Wawan Abdul Muin", cabang = "Cabang Gemolong", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Budi", cabang = "Cabang Gemolong 2", gender = "L", presence = "SAKIT"))
        listWarga.add(StatWarga(name = "Wawan Setiawan", cabang = "Cabang Gemolong", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Hartanto", cabang = "Cabang Gemolong 2", gender = "L"))
        listWarga.add(StatWarga(name = "Siti Maemunah", cabang = "Cabang Gemolong", gender = "P"))
        listWarga.add(StatWarga(name = "Zahrotun", cabang = "Cabang Gemolong 2", gender = "P"))
        listWarga.add(StatWarga(name = "Setyowati", cabang = "Cabang Gemolong", gender = "P"))
        listWarga.add(StatWarga(name = "Sri Ningsih", cabang = "Cabang Gemolong 2", gender = "P"))

        listWarga.add(StatWarga(name = "Wawan Abdul Muin", cabang = "Cabang Gemolong", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Budi", cabang = "Cabang Gemolong 2", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Setiawan", cabang = "Cabang Gemolong", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Hartanto", cabang = "Cabang Gemolong 2", gender = "L"))
        listWarga.add(StatWarga(name = "Siti Maemunah", cabang = "Cabang Gemolong", gender = "P"))
        listWarga.add(StatWarga(name = "Zahrotun", cabang = "Cabang Gemolong 2", gender = "P", presence = "SAKIT"))
        listWarga.add(StatWarga(name = "Setyowati", cabang = "Cabang Gemolong", gender = "P"))
        listWarga.add(StatWarga(name = "Sri Ningsih", cabang = "Cabang Gemolong 2", gender = "P"))

        listWarga.add(StatWarga(name = "Wawan Abdul Muin", cabang = "Cabang Gemolong", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Budi", cabang = "Cabang Gemolong 2", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Setiawan", cabang = "Cabang Gemolong", gender = "L"))
        listWarga.add(StatWarga(name = "Wawan Hartanto", cabang = "Cabang Gemolong 2", gender = "L"))
        listWarga.add(StatWarga(name = "Siti Maemunah", cabang = "Cabang Gemolong", gender = "P"))
        listWarga.add(StatWarga(name = "Zahrotun", cabang = "Cabang Gemolong 2", gender = "P"))
        listWarga.add(StatWarga(name = "Setyowati", cabang = "Cabang Gemolong", gender = "P"))
        listWarga.add(StatWarga(name = "Sri Ningsih", cabang = "Cabang Gemolong 2", gender = "P"))
        return listWarga
    }

    fun fetchData(
        sortType: Int = 0,
        filter: String = PresenceStatusEnum.HADIR.value,
        cached:Boolean = false
    ){
        if(cached){
            val groupedListWarga = StatisticExtension.groupAndSort(_listWarga.value!!, filter, sortType)
            _groupedListWarga.postValue(groupedListWarga)
            updateCounter(groupedListWarga)
            updateCabang(groupedListWarga)
        }else{
            statisticService.getPresences(
                token = token.value!!,
                eventId = selectedEventId.value!!,
                onSuccess = {response ->
                    _listWarga.postValue(response)
                    val groupedListWarga = StatisticExtension.groupAndSort(response, filter, sortType)
                    _groupedListWarga.postValue(groupedListWarga)
                    updateCounter(groupedListWarga)
                    updateCabang(groupedListWarga)
                },
                onError = {message ->
                    _errorMessage.postValue(message)
                }
            )
        }
    }

    private fun updateCabang(value:List<List<StatWarga>>){
        val cabang = StatisticExtension.currentCabang(value)
        _cabang.postValue(cabang)
    }

    private fun updateCounter(value:List<List<StatWarga>>){
        val genderCounter = StatisticExtension.countGender(value)
        _totalMale.postValue(genderCounter.male)
        _totalFemale.postValue(genderCounter.female)
    }
}