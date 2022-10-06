package or.id.mta.presensi.event.entity

import java.time.LocalDateTime

data class EventResponse(
    var id:Int,
    var office_id:Int,
    var office_name:String,
    var name:String,
    var start_at:String,
    var end_at:String,
    var is_active:Int,
    var created_at:String,
    var updated_at:String,
)
