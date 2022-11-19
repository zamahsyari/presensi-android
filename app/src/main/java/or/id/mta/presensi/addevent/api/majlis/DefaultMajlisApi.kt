package or.id.mta.presensi.addevent.api.majlis

import android.content.Context
import android.util.Log
import okhttp3.*
import or.id.mta.presensi.R
import or.id.mta.presensi.addevent.entity.majlis.MajlisApiResponse
import or.id.mta.presensi.login.util.MajlisConverter
import java.io.IOException


class DefaultMajlisApi(context:Context, client: OkHttpClient): MajlisApi {
    val client = client
    val url = context.getString(R.string.base_url)+ "/majlis"

    override fun getPerwakilan(
        token: String,
        filter: MajlisApiFilter,
        onSuccess: (response: MajlisApiResponse) -> Unit,
        onError: (message: String) -> Unit
    ) {
        var finalUrl = "$url/perwakilan?per_page=200"
        if(filter.name != null && filter.name != ""){
            finalUrl += "?filter[]=office_name:${filter.name}"
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
                    val eventResponse = MajlisConverter.stringToMajlisApiResponse(responseString)
                    onSuccess(eventResponse)
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }

    override fun getCabang(
        token: String,
        filter: MajlisApiFilter,
        perwakilanCode: String,
        onSuccess: (response: MajlisApiResponse) -> Unit,
        onError: (message: String) -> Unit
    ) {
        var finalUrl = "$url/cabang?per_page=200&perwakilan=$perwakilanCode"
        if(filter.name != null && filter.name != ""){
            finalUrl += "&filter[]=office_name:${filter.name}"
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
                    val eventResponse = MajlisConverter.stringToMajlisApiResponse(responseString)
                    onSuccess(eventResponse)
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }
}