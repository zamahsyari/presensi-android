package or.id.mta.presensi.login.service

interface LoginService {
    fun login(
        username:String,
        password:String,
        onSuccess: (token: String, officeId: Int) -> Unit,
        onError: (message: String) -> Unit
    )
}