package or.id.mta.presensi.scan.entity.presence

data class PresenceApiRequest(
    var member_id: Int,
    var event_id:Int,
    var note:String,
    var permit_type:Int
)