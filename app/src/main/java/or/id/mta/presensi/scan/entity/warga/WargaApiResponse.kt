package or.id.mta.presensi.scan.entity.warga

data class WargaApiResponse(
    var data: List<WargaApiEntity>,
    var total_data: Int,
    var total_page: Int
)
