package or.id.mta.presensi.statistic.util

enum class PresenceStatusEnum(val value: String) {
    HADIR("hadir".uppercase()),
    SAKIT("sakit".uppercase()),
    KERJA("kerja".uppercase()),
    PULANG_KAMPUNG("pulang kampung".uppercase()),
    LAIN_LAIN("lain-lain".uppercase())
}