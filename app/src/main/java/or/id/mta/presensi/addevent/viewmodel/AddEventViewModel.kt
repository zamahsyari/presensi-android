package or.id.mta.presensi.addevent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import or.id.mta.presensi.addevent.entity.addevent.AddEventEntity
import or.id.mta.presensi.addevent.entity.majlis.MajlisEntity
import or.id.mta.presensi.login.service.AddEventService
import java.util.*

class AddEventViewModel(token: LiveData<String>, addEventService: AddEventService): ViewModel() {
    val service = addEventService
    val token = token

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR)
    val minute = calendar.get(Calendar.MINUTE)

    private var _perwakilanEntity = MutableLiveData<MajlisEntity>()
    val perwakilanEntity: LiveData<MajlisEntity> = _perwakilanEntity

    private var _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private var _query = MutableLiveData("")
    val query: LiveData<String> = _query

    private var _selectedPerwakilanCode = MutableLiveData("")
    val selectedPerwakilanCode: LiveData<String> = _selectedPerwakilanCode

    private var _perwakilanEntities = MutableLiveData(emptyList<MajlisEntity>())
    val perwakilanEntities: LiveData<List<MajlisEntity>> = _perwakilanEntities

    private var _cabangEntities = MutableLiveData(emptyList<MajlisEntity>())
    val cabangEntities: LiveData<List<MajlisEntity>> = _cabangEntities

    private var _selectedPerwakilan = MutableLiveData(0)
    val selectedPerwakilan: LiveData<Int> = _selectedPerwakilan

    private var _selectedCabang = MutableLiveData(0)
    val selectedCabang: LiveData<Int> = _selectedCabang

    private var _formName = MutableLiveData("")
    val formName: LiveData<String> = _formName

    private var _formDescription = MutableLiveData("")
    val formDescription: LiveData<String> = _formDescription

    private var _selectedStartDate = MutableLiveData("$year-${formatDecimalToString(month+1)}-${formatDecimalToString(day)}")
    val selectedStartDate: LiveData<String> = _selectedStartDate

    private var _selectedStartTime = MutableLiveData("${formatDecimalToString(hour)}:${formatDecimalToString(minute)}")
    val selectedStartTime: LiveData<String> = _selectedStartTime

    private var _selectedEndTime = MutableLiveData("${formatDecimalToString(hour)}:${formatDecimalToString(minute)}")
    val selectedEndTime: LiveData<String> = _selectedEndTime

    private var _selectedIsActive = MutableLiveData(false)
    val selectedIsActive: LiveData<Boolean> = _selectedIsActive

    private var _selectedInterval = MutableLiveData("HARIAN")
    val selectedInterval: LiveData<String> = _selectedInterval

    fun formatDecimalToString(value:Int): String {
        if(value < 10){
            return "0" + value
        }else{
            return "" + value
        }
    }

    fun setSelectedPerwakilanCode(code:String){
        _selectedPerwakilanCode.value = code
    }

    fun setSelectedPerwakilan(id:Int){
        _selectedPerwakilan.value = id
    }

    fun setPerwakilanEntity(majlisEntity: MajlisEntity){
        var majlis = majlisEntity
        majlis.name = "${majlis.name} (Perwakilan)"
        _perwakilanEntity.postValue(majlisEntity)
    }

    fun setSelectedCabang(id:Int){
        _selectedCabang.value = id
    }

    fun setSelectedStartDate(value:String){
        _selectedStartDate.value = value
    }

    fun setSelectedStartTime(value:String){
        _selectedStartTime.value = value
    }

    fun setSelectedEndTime(value:String){
        _selectedEndTime.value = value
    }

    fun setSelectedIsActive(value:Boolean){
        _selectedIsActive.value = value
    }

    fun setName(value:String){
        _formName.value = value
    }

    fun setDescription(value:String){
        _formDescription.value = value
    }

    fun setInterval(value:String){
        _selectedInterval.value = value
    }

    fun fetchPerwakilan(){
        service.getPerwakilan(
            token = token.value!!,
            filterName = query.value,
            onSuccess = {majlisEntities ->
                _perwakilanEntities.postValue(majlisEntities)
                _selectedPerwakilanCode.postValue(majlisEntities.get(0).code)
                _selectedPerwakilan.postValue(majlisEntities.get(0).id)
                setPerwakilanEntity(majlisEntities.get(0))
                fetchCabang()
            },
            onError = {message ->
                _errorMessage.postValue(message)
            }
        )
    }

    fun fetchCabang(){
        service.getCabang(
            token = token.value!!,
            perwakilanCode = selectedPerwakilanCode.value!!,
            filterName = query.value,
            onSuccess = {majlisEntities ->
                var cabangs = mutableListOf<MajlisEntity>()
                cabangs.add(perwakilanEntity.value!!)
                cabangs.addAll(majlisEntities)

                _cabangEntities.postValue(cabangs)
                if(majlisEntities.size > 0){
                    _selectedCabang.postValue(majlisEntities.get(0).id)
                }else{
                    _selectedCabang.postValue(selectedPerwakilan.value)
                }
            },
            onError = {message ->
                _errorMessage.postValue(message)
            }
        )
    }

    fun isValidated(): Boolean{
        if(selectedPerwakilan.value == null || selectedPerwakilan.value!! == 0){
            return false
        }
        if(formName.value == null || formName.value!!.isEmpty()){
            return false
        }
        if(formDescription.value == null || formDescription.value!!.isEmpty()){
            return false
        }
        if(selectedStartDate.value == null){
            return false
        }
        if(selectedStartTime.value == null){
            return false
        }
        if(selectedEndTime.value == null){
            return false
        }
        if(selectedIsActive.value == null){
            return false
        }
        if(selectedInterval.value == null){
            return false
        }
        return true
    }

    fun submit(onSuccess: () -> Unit){
        if(isValidated()){
            var officeId = selectedCabang.value!!
            if(selectedCabang.value!! == 0){
                officeId = selectedPerwakilan.value!!
            }

            service.addEvent(
                token = token.value!!,
                addEventEntity = AddEventEntity(
                    officeId = officeId,
                    name = formName.value!!,
                    description = formDescription.value!!,
                    startDate = selectedStartDate.value!!,
                    startTime = selectedStartTime.value!!,
                    endTime = selectedEndTime.value!!,
                    isActive = selectedIsActive.value!!,
                    interval = selectedInterval.value!!
                ),
                {
                    _errorMessage.postValue("")
                    onSuccess()
                },
                {errorMessage ->
                    _errorMessage.postValue(errorMessage)
                }
            )
        }else{
            _errorMessage.value = "Silakan lengkapi form"
        }
    }
}