package or.id.mta.presensi.login.service

import androidx.compose.ui.platform.LocalContext
import or.id.mta.presensi.login.api.LoginApi
import or.id.mta.presensi.login.util.LoginConverter

class DefaultLoginService(repository: LoginApi):LoginService {
    val repo:LoginApi = repository

    override fun login(
        username:String,
        password:String,
        onSuccess: (message: String) -> Unit,
        onError: (message: String) -> Unit
    ){
        val loginRequest = LoginConverter.toLoginRequest(username, password)
        repo.login(
            loginRequest,
            {response ->
                onSuccess(response.token)
            },
            {errorMessage ->
                onError(errorMessage)
            }
        )
    }
}