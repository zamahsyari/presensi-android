package or.id.mta.presensi.event.entity

import androidx.compose.ui.graphics.vector.ImageVector

class EventItemEntity (
    var id: Int,
    var icon: ImageVector,
    var title: String,
    var description: String,
    var isActive: Boolean
)