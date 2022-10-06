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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.event.entity.EventItemEntity
import or.id.mta.presensi.ui.theme.PrimaryGreen
import or.id.mta.presensi.ui.theme.PrimaryGrey

@Composable
fun EventHeader(onAddEventClick: () -> Unit){
    Row(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Box(modifier = Modifier.weight(weight = 1F))
        Text(text = "Pilih Acara",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Box(modifier = Modifier.weight(weight = 1F))
        Box(
            modifier = Modifier.clickable {onAddEventClick()}
        ){
            Icon(Icons.Filled.Add, "add event")
        }
    }
}

@Composable
fun SearchForm(value:String, placeholder: String, onChange: (String) -> Unit) {
    Box(){
        TextField(
            value = value,
            onValueChange = {onChange(it)},
            placeholder = { Text(text = placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, color = PrimaryGrey, RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                placeholderColor = PrimaryGrey,
                backgroundColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 12.sp
            )
        )
        Icon(
            Icons.Filled.Search,
            "menu",
            modifier = Modifier
                .padding(
                    start = LocalConfiguration.current.screenWidthDp.dp - 80.dp,
                    top = 16.dp
                )
                .width(24.dp)
        )
    }
}

@Composable
fun GenerateItems(
    eventEntities: List<EventEntity>,
    onClick: (Int) -> Unit,
    onSelectEvent: (EventEntity) -> Unit
){
    val eventItemEntities = mutableListOf<EventItemEntity>()
    eventEntities.forEach {eventEntity ->
        val item = EventItemEntity(
            id = eventEntity.id,
            icon = Icons.Filled.DateRange,
            title = eventEntity.name,
            description = eventEntity.cabang,
            isActive = eventEntity.isActive
        )
        eventItemEntities.add(item)
    }
    EventItems(entities = eventItemEntities, onClick = {
        onClick(it)
        val selectedEventEntity = selectEventEntity(eventEntities, it)
        onSelectEvent(selectedEventEntity)
    })
}

private fun selectEventEntity(eventEntities: List<EventEntity>, id:Int):EventEntity{
    return eventEntities.filter { it.id == id }[0]
}

@Composable
fun EventItems(entities: List<EventItemEntity>, onClick: (Int) -> Unit){
    LazyColumn {
        items(entities.size){index ->
            EventItem(item = entities.get(index), onClick = { onClick(it) })
        }
    }
}

@Composable
fun EventItem(item: EventItemEntity, onClick: (Int) -> Unit){
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
        Box(Modifier.weight(1F))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            if (item.isActive){
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(8.dp)
                        .height(8.dp)
                        .background(color = PrimaryGreen)
                )
            }else{
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .width(8.dp)
                        .height(8.dp)
                        .background(color = PrimaryGrey)
                )
            }
        }
    }
}