package or.id.mta.presensi.addevent.api.addevent

import android.content.Context
import android.util.Log
import okhttp3.*
import or.id.mta.presensi.R
import or.id.mta.presensi.addevent.entity.addevent.AddEventApiRequest
import or.id.mta.presensi.addevent.util.AddEventConverter
import or.id.mta.presensi.login.util.LoginConverter
import or.id.mta.presensi.login.util.MajlisConverter
import java.io.IOException

class DefaultAddEventApi(context: Context, client: OkHttpClient): AddEventApi {
    val client = client
    val url = context.getString(R.string.base_url)+ "/events"

    override fun addEvent(
        token: String,
        request: AddEventApiRequest,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        val requestBody = AddEventConverter.addEventApiRequestToRequestBody(request)
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Kesalahan jaringan")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = "${response.body()?.string()}"
                Log.d("HTTP", responseString)
                if(response.code() == 200){
                    onSuccess()
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }
}