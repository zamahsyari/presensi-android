package or.id.mta.presensi.event.api

import android.content.Context
import android.util.Log
import okhttp3.*
import or.id.mta.presensi.R
import or.id.mta.presensi.event.entity.EventApiResponse
import or.id.mta.presensi.login.util.EventConverter
import java.io.IOException


class DefaultEventApi(context:Context, client: OkHttpClient): EventApi {
    val client = client
    val url = context.getString(R.string.base_url) + "/events?1=1"

    override fun getAll(
        token: String,
        filter: EventApiFilter,
        onSuccess: (response: EventApiResponse) -> Unit,
        onError: (message: String) -> Unit
    ) {
        var finalUrl = url
        if(filter.name != null && filter.name != ""){
            finalUrl += "&filter[]=name:${filter.name}"
        }
        if(filter.officeId != null && filter.officeId != 0){
            finalUrl += "&filter[]=office_id:${filter.officeId}"
        }
        val request = Request.Builder()
            .url(finalUrl)
            .header("Authorization", "Bearer $token")
            .build()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                onError("Kesalahan jaringan")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = "${response.body()?.string()}"
                Log.d("HTTP", responseString)
                if(response.code() == 200){
                    val eventResponse = EventConverter.stringToEventApiResponse(responseString)
                    onSuccess(eventResponse)
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }
}