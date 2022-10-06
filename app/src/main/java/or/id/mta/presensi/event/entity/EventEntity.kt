package or.id.mta.presensi.event.entity

import java.time.LocalDateTime

data class EventEntity(
    var id:Int,
    var cabang_id:Int,
    var cabang:String,
    var name:String,
    var startAt:LocalDateTime,
    var endAt:LocalDateTime,
    var isActive:Boolean
)
