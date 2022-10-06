package or.id.mta.presensi.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import or.id.mta.presensi.login.service.LoginService

class LoginViewModel(loginService: LoginService): ViewModel() {
    private var _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> = _errorMessage

    private var _successMessage = MutableLiveData("")
    val successMessage: LiveData<String> = _successMessage

    val service = loginService

    fun submit(username:String, password:String, onSuccess: (String) -> Unit){
        service.login(
            username,
            password,
            {token ->
                _successMessage.postValue("Login berhasil")
                _errorMessage.postValue("")
                onSuccess(token)


            },
            {errorMessage ->
                _successMessage.postValue("")
                _errorMessage.postValue(errorMessage)
            }
        )
    }
}