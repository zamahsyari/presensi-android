package or.id.mta.presensi.scan.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import or.id.mta.presensi.R
import or.id.mta.presensi.common.StatisticExtension
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.login.service.ScanService
import or.id.mta.presensi.scan.entity.majlis.MajlisPresence
import or.id.mta.presensi.scan.entity.presence.PresenceEntity
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.statistic.entity.StatWarga
import or.id.mta.presensi.statistic.util.PresenceStatusEnum
import java.util.*
import kotlin.concurrent.schedule

class ScanViewModel(
    val context: Context,
    val token: LiveData<String>,
    val serialNumber: LiveData<String>,
    val memberId: LiveData<Int>,
    val event: LiveData<EventEntity>,
    val scanService: ScanService):ViewModel() {

//    stats
    private var _cabang = MutableLiveData("")
    val cabang: LiveData<String> = _cabang

    private var _listWarga = MutableLiveData<List<StatWarga>>()
    val listWarga: LiveData<List<StatWarga>> = _listWarga

    private var _groupedListWarga = MutableLiveData<List<List<StatWarga>>>()
    val groupedListWarga: LiveData<List<List<StatWarga>>> = _groupedListWarga

    private var _totalMale = MutableLiveData(0)
    val totalMale: LiveData<Int> = _totalMale

    private var _totalFemale = MutableLiveData(0)
    val totalFemale: LiveData<Int> = _totalFemale

//    presence
    private var _userPresence = MutableLiveData<UserPresence>(null)
    val userPresence: LiveData<UserPresence> = _userPresence

    private var _majlisPresence = MutableLiveData<MajlisPresence>()
    val majlisPresence: LiveData<MajlisPresence> = _majlisPresence

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isUserFound = MutableLiveData(false)
    val isUserFound: LiveData<Boolean> = _isUserFound

    private var _isUserNotFound = MutableLiveData(false)
    val isUserNotFound: LiveData<Boolean> = _isUserNotFound

    private var _isEnabled = MutableLiveData(false)
    val isEnabled: LiveData<Boolean> = _isEnabled

    private var _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private var _selectedPresenceType = MutableLiveData("HADIR")
    val selectedPresenceType: LiveData<String> = _selectedPresenceType

    private var _selectedPermitDetail = MutableLiveData("KERJA")
    val selectedPermitDetail: LiveData<String> = _selectedPermitDetail

    private var _otherPermission = MutableLiveData("")
    val otherPermission: LiveData<String> = _otherPermission

    val mediaPlayerSuccess = MediaPlayer.create(context, R.raw.silakan_masuk)
    val mediaPlayerFailed = MediaPlayer.create(context, R.raw.anda_belum_terdaftar)

    fun setOtherPermission(value:String){
        _otherPermission.value = value
    }

    fun setPresenceType(value:String){
        _selectedPresenceType.value = value
    }

    fun setPermitDetail(value:String){
        _selectedPermitDetail.value = value
    }

    fun setLoading(value:Boolean){
        _isLoading.postValue(value)
    }

    fun setEnabled(value:Boolean){
        _isEnabled.postValue(value)
    }

    fun setUserFound(value:Boolean){
        _isUserFound.postValue(value)
    }

    fun setUserNotFound(value:Boolean){
        _isUserNotFound.postValue(value)
    }

    fun generateUserPresence(): UserPresence {
        return UserPresence(
            id = 1,
            name = "John Doe",
            cabang = "Ngemplak",
            age = 28,
            gender = "MALE"
        )
    }

    fun generateMajlisPresence(): MajlisPresence {
        return MajlisPresence(
            name = "Kajian Gelombang",
            cabang = "Ngemplak",
            maleCount = 28,
            femaleCount = 25
        )
    }

    fun findWarga(isAutoClosed: Boolean = false){
        _isLoading.value = true
        scanService.getWarga(
            token = token.value!!,
            serialNumber = serialNumber.value!!,
            memberId = memberId.value!!,
            onSuccess = {userPresence ->
                Log.d("USER PRESENCE", userPresence.toString())
                mediaPlayerSuccess.start()
                _isLoading.postValue(false)
                _isUserFound.postValue(true)
                _userPresence.postValue(userPresence)
                if(isAutoClosed){
                    Timer().schedule(500){
                        submit {}
                        _isUserFound.postValue(false)
                    }
                }
            },
            onError = {errorMessage ->
                mediaPlayerFailed.start()
                _errorMessage.postValue(errorMessage)
                _isLoading.postValue(false)
                _isUserNotFound.postValue(true)
                if(isAutoClosed){
                    Timer().schedule(3000){
                        _isUserNotFound.postValue(false)
                    }
                }
            }
        )
    }

    fun submit(onSuccess: () -> Unit){
        scanService.presence(
            token = token.value!!,
            presenceEntity = PresenceEntity(
                memberId = userPresence.value!!.id,
                eventId = event.value!!.id,
                presenceType = selectedPresenceType.value!!,
                permitDetail = selectedPermitDetail.value!!,
                note = otherPermission.value!!
            ),
            {
                _errorMessage.postValue("")
                updateStatistic(event.value!!.id)
                onSuccess()
            },
            {errorMessage ->
                _errorMessage.postValue(errorMessage)
            }
        )
    }

    fun initEvent(){
        if(event.value != null){
            val majlisPresence = MajlisPresence(
                name = event.value!!.name,
                cabang = event.value!!.cabang,
                maleCount = 0,
                femaleCount = 0
            )
            _majlisPresence.postValue(majlisPresence)
        }
    }

    fun updateStatistic(eventId:Int){
        scanService.getPresences(
            token = token.value!!,
            eventId = eventId,
            onSuccess = {response ->
                _listWarga.postValue(response)
                val groupedListWarga = StatisticExtension.groupAndSort(response, PresenceStatusEnum.HADIR.value, 0)
                _groupedListWarga.postValue(groupedListWarga)
                val genderCounter = StatisticExtension.countGender(groupedListWarga)
                val majlisPresence = MajlisPresence(
                    name = event.value!!.name,
                    cabang = event.value!!.cabang,
                    maleCount = genderCounter.male,
                    femaleCount = genderCounter.female
                )
                _majlisPresence.postValue(majlisPresence)
            },
            onError = {message ->
                Log.d("UPDATE STATISTIC", message.toString())
                _errorMessage.postValue(message)
            }
        )
    }
}