package or.id.mta.presensi.addevent.entity.majlis

data class MajlisApiResponse(
    var data:List<MajlisResponse>,
    var total_data:Int,
    var total_page:Int
)
