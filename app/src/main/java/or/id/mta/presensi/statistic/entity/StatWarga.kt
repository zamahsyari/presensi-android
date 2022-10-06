package or.id.mta.presensi.statistic.entity

data class StatWarga(
    var name: String,
    var cabang: String,
    var gender: String,
    var presence: String = "HADIR"
)
