package or.id.mta.presensi.scan.entity.presence

data class PresenceApiEntity(
    var id:Int,
    var member_id:Int,
    var event_id:Int,
    var note:String,
    var permit_type:Int,
    var created_at:String,
    var updated_at:String,
    var office_name:String,
    var name:String,
    var member_name:String,
    var member_gender:Int
)
