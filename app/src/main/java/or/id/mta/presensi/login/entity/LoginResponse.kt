package or.id.mta.presensi.login.entity

data class LoginResponse(
    var id:Int,
    var username:String,
    var token:String,
    var office_id:Int
)
