package or.id.mta.presensi.scan.api.scan

import android.content.Context
import android.util.Log
import okhttp3.*
import or.id.mta.presensi.R
import or.id.mta.presensi.scan.entity.presence.PresenceApiEntity
import or.id.mta.presensi.scan.entity.presence.PresenceApiRequest
import or.id.mta.presensi.scan.util.ScanConverter
import java.io.IOException

class DefaultScanApi(context: Context, client: OkHttpClient): ScanApi {
    val client = client
    val url = context.getString(R.string.base_url)+ "/presences"

    override fun presence(
        token: String,
        request: PresenceApiRequest,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        val requestBody = ScanConverter.presenceApiRequestToRequestBody(request)
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

    override fun getPresences(
        token: String,
        filter: ScanApiFilter,
        onSuccess: (List<PresenceApiEntity>) -> Unit,
        onError: (message: String) -> Unit
    ) {
        var finalUrl = url
        if(filter.eventId != null && filter.eventId != 0){
            finalUrl += "?filter[]=event_id:${filter.eventId}"
        }
        val request = Request.Builder()
            .url(finalUrl)
            .header("Authorization", "Bearer $token")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Kesalahan jaringan")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseString = "${response.body()?.string()}"
                Log.d("HTTP", responseString)
                if(response.code() == 200){
                    val eventResponse = ScanConverter.stringToPresenceApiResponse(responseString)
                    onSuccess(eventResponse.data)
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }
}