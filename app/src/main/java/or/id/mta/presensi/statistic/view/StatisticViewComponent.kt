package or.id.mta.presensi.statistic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import or.id.mta.presensi.R
import or.id.mta.presensi.statistic.entity.StatWarga
import or.id.mta.presensi.statistic.util.GenderEnum
import or.id.mta.presensi.ui.theme.PrimaryGrey

@Composable
fun StatisticHeader(
    onChange: (index: Int) -> Unit,
    onBackClick: () -> Unit
){
    Row(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Icon(Icons.Filled.ArrowBack, "back",
            modifier = Modifier.clickable { onBackClick() })
        Box(modifier = Modifier.weight(weight = 1F))
        Text(text = "Daftar Kehadiran",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Box(modifier = Modifier.weight(weight = 1F))
        FilterDropdown(
            items = listOf("A-Z", "Z-A"),
            onChange = { onChange(it) }
        )
    }
}

@Composable
fun PresenceDropdown(
    items: List<String>,
    onChange: (value: String) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Column{
        Box{
            Row(
                modifier = Modifier.clickable {
                    expanded = true
                }
            ){
                Text(items[selectedIndex],
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.size(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        "down",
                        Modifier.size(12.dp)
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, color = PrimaryGrey, RoundedCornerShape(8.dp))
            ) {
                items.forEachIndexed {idx, item ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        selectedIndex = idx
                        onChange(item)
                    }) {
                        Text(item)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDropdown(
    items: List<String>,
    onChange: (index: Int) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Column{
        Box{
            if(selectedIndex == 0){
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.ic_filter
                    ), "filter",
                    modifier = Modifier.clickable {expanded = true}
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.ic_filter_reversed
                    ), "filter",
                    modifier = Modifier.clickable {expanded = true}
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, color = PrimaryGrey, RoundedCornerShape(8.dp))
            ) {
                items.forEachIndexed {idx, item ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        selectedIndex = idx
                        onChange(selectedIndex)
                    }) {
                        Text(item)
                    }
                }
            }
        }
    }
}

fun translateGender(value:String): String{
    if(value == GenderEnum.MALE.value){
        return "Putra"
    }
    return "Putri"
}

fun isEvenNumber(value:Int): Boolean{
    if(value % 2 != 0){
        return true
    }
    return false
}

@Composable
fun StatisticListWarga(groupedListWarga: List<List<StatWarga>>){
    LazyColumn(
        modifier = Modifier
            .height(LocalConfiguration.current.screenHeightDp.dp - 162.dp)
    ){
        items(groupedListWarga){listWarga ->
            Box(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp)
            ){
                if(!listWarga.isEmpty()){
                    Text(translateGender(listWarga.get(0).gender),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Box(modifier = Modifier.height(16.dp))
            listWarga.forEachIndexed {index, warga ->
                if(!isEvenNumber(index)){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFF9F9F9))
                    ){
                        Box(
                            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                        ){
                            StatisticWargaItem(warga = warga)
                        }
                    }
                }else{
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFFFFFFFF))
                    ){
                        Box(
                            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                        ){
                            StatisticWargaItem(warga = warga)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatisticWargaItem(warga: StatWarga){
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
    ){
        Row{
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryGrey),
            ){
                Icon(Icons.Filled.Person, "person", Modifier.size(24.dp))
            }
            Box(modifier = Modifier.width(8.dp))
            Column {
                Text(warga.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(warga.cabang,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}