package or.id.mta.presensi.event.entity

data class EventApiResponse(
    var data: List<EventResponse>,
    var total_data: Int,
    var total_page: Int
)
