package or.id.mta.presensi.login.util

import com.google.gson.Gson
import or.id.mta.presensi.addevent.entity.majlis.MajlisApiResponse
import or.id.mta.presensi.addevent.entity.majlis.MajlisEntity

class MajlisConverter {
    companion object{
        fun stringToMajlisApiResponse(value:String): MajlisApiResponse {
            val gson = Gson()
            return gson.fromJson(value, MajlisApiResponse::class.java)
        }

        fun majlisApiResponseToMajlisEntities(response: MajlisApiResponse): List<MajlisEntity>{
            val majlisEntities = mutableListOf<MajlisEntity>()
            response.data.forEach {majlisResponse ->
                val majlisEntity = MajlisEntity(
                    id = majlisResponse.office_id,
                    code = majlisResponse.office_code,
                    name = majlisResponse.office_name,
                    address = majlisResponse.office_address,
                    phone = majlisResponse.office_phone
                )
                majlisEntities.add(majlisEntity)
            }
            return majlisEntities
        }
    }
}