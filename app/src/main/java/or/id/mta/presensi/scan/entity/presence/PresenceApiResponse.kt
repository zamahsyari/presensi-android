package or.id.mta.presensi.scan.entity.presence

data class PresenceApiResponse(
    var data: List<PresenceApiEntity>,
    var total_data: Int,
    var total_page: Int
)
