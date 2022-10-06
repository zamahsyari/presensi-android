package or.id.mta.presensi.addevent.entity.addevent

data class AddEventApiRequest(
    var office_id:Int,
    var name:String,
    var start_at:String,
    var end_at:String,
    var is_active:Boolean,
    var description:String,
    var interval:String
)
