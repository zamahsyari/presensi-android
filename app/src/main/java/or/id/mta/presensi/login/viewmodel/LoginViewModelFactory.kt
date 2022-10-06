package or.id.mta.presensi.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import or.id.mta.presensi.login.service.LoginService

class LoginViewModelFactory(loginService: LoginService): ViewModelProvider.Factory {
    var loginService = loginService
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginService = loginService) as T
    }
}