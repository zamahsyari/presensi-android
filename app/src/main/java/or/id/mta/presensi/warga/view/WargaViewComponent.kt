package or.id.mta.presensi.warga.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.ui.theme.PrimaryGreen
import or.id.mta.presensi.ui.theme.PrimaryGrey
import or.id.mta.presensi.warga.entity.WargaItemEntity

@Composable
fun WargaHeader(onBackClick: () -> Unit){
    Row(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier.clickable { onBackClick() }
        ){
            Icon(Icons.Filled.ArrowBack, "back")
        }
        Box(modifier = Modifier.weight(weight = 1F))
        Text(text = "Cari Warga",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Box(modifier = Modifier.weight(weight = 1F))
    }
}

@Composable
fun GenerateWargaItems(userPresences: List<UserPresence>, onClick: (Int) -> Unit){
    val wargaItemEntities = mutableListOf<WargaItemEntity>()
    userPresences.forEach {eventEntity ->
        val item = WargaItemEntity(
            id = eventEntity.id,
            icon = Icons.Filled.Person,
            title = eventEntity.name,
            description = eventEntity.cabang,
        )
        wargaItemEntities.add(item)
    }
    WargaItems(entities = wargaItemEntities, onClick = { onClick(it) })
}

@Composable
fun WargaItems(entities: List<WargaItemEntity>, onClick: (Int) -> Unit){
    LazyColumn {
        items(entities.size){index ->
            WargaItem(item = entities.get(index), onClick = { onClick(it) })
        }
    }
}

@Composable
fun WargaItem(item: WargaItemEntity, onClick: (Int) -> Unit){
    Row(
        modifier = Modifier.padding(bottom = 16.dp).clickable { onClick(item.id) }
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .width(40.dp)
                .height(40.dp)
                .background(color = PrimaryGrey),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, "icon", modifier = Modifier.width(24.dp))
        }
        Box(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
        ) {
            Text(text = item.title, fontWeight = FontWeight.Medium, fontSize = 12.sp )
            Text(text = item.description, fontWeight = FontWeight.Light, fontSize = 10.sp )
        }
    }
}