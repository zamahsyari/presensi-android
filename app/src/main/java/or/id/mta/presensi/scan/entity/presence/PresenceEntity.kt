package or.id.mta.presensi.scan.entity.presence

data class PresenceEntity(
    var memberId:Int,
    var eventId:Int,
    var presenceType:String,
    var permitDetail:String,
    var note:String,
)
