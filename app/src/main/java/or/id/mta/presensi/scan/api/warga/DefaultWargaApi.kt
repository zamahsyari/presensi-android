package or.id.mta.presensi.scan.api.warga

import android.content.Context
import android.util.Log
import okhttp3.*
import or.id.mta.presensi.R
import or.id.mta.presensi.scan.entity.warga.WargaApiResponse
import or.id.mta.presensi.scan.util.ScanConverter
import java.io.IOException

class DefaultWargaApi(context: Context, client: OkHttpClient): WargaApi {
    val client = client
    val url = context.getString(R.string.base_url)+ "/warga"

    override fun getWargaBySerialNumber(
        token: String,
        serialNumber: String,
        onSuccess: (response: WargaApiResponse) -> Unit,
        onError: (message: String) -> Unit
    ) {
        var finalUrl = "$url?filter[]=member_card_id:$serialNumber"
        Log.d("URL", finalUrl)
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
                    val wargaResponse = ScanConverter.stringToWargaApiResponse(responseString)
                    onSuccess(wargaResponse)
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }

    override fun getWargaById(
        token: String,
        memberId: Int,
        onSuccess: (response: WargaApiResponse) -> Unit,
        onError: (message: String) -> Unit
    ) {
        var finalUrl = "$url?filter[]=member_id:$memberId"
        Log.d("URL", finalUrl)
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
                    val wargaResponse = ScanConverter.stringToWargaApiResponse(responseString)
                    onSuccess(wargaResponse)
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }

    override fun getWargaByName(
        token: String,
        filter: WargaApiFilter,
        onSuccess: (response: WargaApiResponse) -> Unit,
        onError: (message: String) -> Unit
    ) {
        var finalUrl = "$url?1=1"
        if(filter.name != null && filter.name != ""){
            finalUrl = "$finalUrl&filter[]=member_name:${filter.name}"
        }
        if(filter.officeId != null && filter.officeId != 0){
            finalUrl = "$finalUrl&filter[]=office_id:${filter.officeId}"
        }
        Log.d("URL", finalUrl)
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
                    val wargaResponse = ScanConverter.stringToWargaApiResponse(responseString)
                    onSuccess(wargaResponse)
                }else{
                    onError("Kesalahan server")
                }
            }
        })
    }
}