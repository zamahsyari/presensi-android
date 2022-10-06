package or.id.mta.presensi.login.util

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import or.id.mta.presensi.login.entity.LoginRequest
import or.id.mta.presensi.login.entity.LoginResponse

class LoginConverter {
    companion object{
        fun loginRequestToRequestBody(loginRequest: LoginRequest): RequestBody{
            val gson = Gson()
            return RequestBody.create(MediaType.get("application/json"), gson.toJson(loginRequest))
        }
        fun toLoginRequest(username:String, password:String): LoginRequest{
            return LoginRequest(username, password)
        }
        fun stringToLoginResponse(value:String): LoginResponse{
            val gson = Gson()
            return gson.fromJson(value, LoginResponse::class.java)
        }
    }
}