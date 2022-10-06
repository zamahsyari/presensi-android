package or.id.mta.presensi.addevent.entity.addevent

data class AddEventEntity(
    var officeId:Int,
    var name:String,
    var description:String,
    var startDate:String,
    var startTime:String,
    var endTime:String,
    var isActive:Boolean,
    var interval:String
)
