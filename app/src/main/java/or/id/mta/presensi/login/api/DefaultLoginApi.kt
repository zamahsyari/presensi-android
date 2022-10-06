package or.id.mta.presensi.login.api

import android.content.Context
import android.util.Log
import okhttp3.*
import or.id.mta.presensi.R
import or.id.mta.presensi.login.entity.LoginRequest
import or.id.mta.presensi.login.entity.LoginResponse
import or.id.mta.presensi.login.util.LoginConverter
import java.io.IOException


class DefaultLoginApi(context:Context, client: OkHttpClient): LoginApi {
    val client = client
    val url = context.getString(R.string.base_url) + "/auth/login"

    override fun login(
        loginRequest: LoginRequest,
        onSuccess: (response: LoginResponse) -> Unit,
        onError: (message: String) -> Unit
    ) {
        val requestBody = LoginConverter.loginRequestToRequestBody(loginRequest)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError("Kesalahan jaringan")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = "${response.body()?.string()}"
                Log.d("HTTP", responseString)
                if(response.code() == 200){
                    val loginResponse = LoginConverter.stringToLoginResponse(responseString)
                    onSuccess(loginResponse)
                }else if(response.code() == 400){
                    onError("Username atau password salah")
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }
}