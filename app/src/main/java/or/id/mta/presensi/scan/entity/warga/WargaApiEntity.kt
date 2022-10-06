package or.id.mta.presensi.scan.entity.warga

data class WargaApiEntity(
    var member_id:Int,
    var member_name:String,
    var office_name:String,
    var member_birthday:String,
    var member_birthplace:String,
    var member_card_id:String,
    var member_gender:Int,
    var member_phone:String,
    var member_address:String,
    var member_ktp:String,
    var member_email:String,
    var member_mustamik:Int,
    var member_blood:String,
    var member_job:String,
    var member_last_education:String
)
