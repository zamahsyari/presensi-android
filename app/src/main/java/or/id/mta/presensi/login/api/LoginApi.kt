package or.id.mta.presensi.login.api

import or.id.mta.presensi.login.entity.LoginRequest
import or.id.mta.presensi.login.entity.LoginResponse

interface LoginApi {
    fun login(
        loginRequest: LoginRequest,
        onSuccess: (response: LoginResponse) -> Unit,
        onError: (message: String) -> Unit
    )
}