package or.id.mta.presensi.scan.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import or.id.mta.presensi.R
import or.id.mta.presensi.common.ButtonForm
import or.id.mta.presensi.common.DropdownForm
import or.id.mta.presensi.common.TextForm
import or.id.mta.presensi.scan.entity.majlis.MajlisPresence
import or.id.mta.presensi.scan.entity.presence.PresenceStatus
import or.id.mta.presensi.scan.entity.user.UserPresence
import or.id.mta.presensi.ui.theme.PrimaryGreen
import or.id.mta.presensi.ui.theme.PrimaryGrey

@Composable
fun ScanHeader(onBackClick: () -> Unit, onSearchClick: () -> Unit){
    Row(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier.clickable { onBackClick() }
        ){
            Icon(Icons.Filled.ArrowBack, "back")
        }
        Box(modifier = Modifier.weight(weight = 1F))
        Text(text = "Presensi",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Box(modifier = Modifier.weight(weight = 1F))
        Box(
            modifier = Modifier.clickable { onSearchClick() }
        ){
            Icon(Icons.Filled.Search, "search warga")
        }
    }
}

@Composable
fun ScanStats(majlisPresence: MajlisPresence){
    Row{
        Column{
            Text(text = majlisPresence.name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(text = majlisPresence.cabang, fontSize = 10.sp, fontWeight = FontWeight.Light)
        }
        Box(modifier = Modifier.weight(1F))
        Column{
            Row{
                Box(
                    modifier = Modifier.height(16.dp), contentAlignment = Alignment.BottomEnd
                ){
                    Text("Pa", fontSize = 10.sp, fontWeight = FontWeight.Light)
                }
                Box(modifier = Modifier.width(4.dp))
                Text(""+majlisPresence.maleCount, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            Row{
                Box(
                    modifier = Modifier.height(16.dp), contentAlignment = Alignment.BottomEnd
                ){
                    Text("Pi", fontSize = 10.sp, fontWeight = FontWeight.Light)
                }
                Box(modifier = Modifier.width(8.dp))
                Text(""+majlisPresence.femaleCount, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun ScanImage(onClick: () -> Unit){
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ){
            Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_tap_reader), contentDescription = "illustration")
        }
        Box(modifier = Modifier.height(32.dp))
        Text("Silakan Tap e-KTP Anda",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.height(8.dp))
        Text("Harap pastikan Anda meletakkan kartu di dekat sensor NFC. Proses pengenalan akan dilakukan secara otomatis",
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun ScanControl(onStatsClick: () -> Unit, enabled:Boolean = false, onChange: (Boolean) -> Unit){
    val isEnabled = remember { mutableStateOf(enabled) }
    Row(
        modifier = Modifier.padding(bottom = 16.dp)
    ){
        Box(
            modifier = Modifier.height(64.dp)
                .clickable {
                    isEnabled.value = !isEnabled.value
                    onChange(isEnabled.value)
                }
            ,
            contentAlignment = Alignment.Center
        ){
            if(isEnabled.value){
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_switch_on), contentDescription = "switch")
            }else{
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_switch_off), contentDescription = "switch")
            }
        }
        Box(modifier = Modifier.weight(weight = 1F))
        Box(
            modifier = Modifier.height(64.dp).clickable { onStatsClick() },
            contentAlignment = Alignment.Center,
        ){
            Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_statistic), contentDescription = "statistic")
        }
    }
}

@Composable
fun ConfirmationPopup(
    isShown: Boolean,
    showConfirmationButton: Boolean = true,
    presenceStatuses: List<PresenceStatus>,
    permitDetails: List<String>,
    userPresence: UserPresence,
    onClose: () -> Unit,
    onSelectPresenceType: (String) -> Unit,
    onSelectPermitDetail: (String) -> Unit,
    onSelectOther: (String) -> Unit,
    onConfirm: () -> Unit
){
    var selectedPresenceStatus by remember { mutableStateOf(1) }
    var _presenceStatuses by remember { mutableStateOf(presenceStatuses) }

    if(isShown){
        AnimatedVisibility(
            visible = isShown,
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5F))
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1F))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(color = Color.White)
                    .padding(24.dp)
                ) {
                    Row{
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = PrimaryGrey)
                                .width(64.dp)
                                .height(64.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                Icons.Filled.Person, "person",
                                modifier = Modifier.size(48.dp))
                        }
                        Box(modifier = Modifier.width(8.dp))
                        Column{
                            Text(userPresence.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium)
                            Text(userPresence.cabang, fontSize = 12.sp)
                            Row{
                                Text("${userPresence.age} tahun",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light)
                                Box(modifier = Modifier.width(4.dp))
                                Text(".",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light)
                                Box(modifier = Modifier.width(4.dp))
                                Text(userPresence.gender.uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light)
                            }
                        }
                        Box(modifier = Modifier.weight(1F))
                        Icon(
                            Icons.Filled.Close, "close",
                            modifier = Modifier.clickable { onClose() })
                    }
                    Box(modifier = Modifier.height(32.dp))
                    if(showConfirmationButton){
                        Row{
                            _presenceStatuses.forEach { status ->
                                if(status.isActive){
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                selectedPresenceStatus = status.id
                                                _presenceStatuses = selectPresenceStatus(
                                                    selectedPresenceStatus,
                                                    _presenceStatuses
                                                )
                                                onSelectPresenceType(status.status)
                                            }
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(color = PrimaryGreen)
                                    ){
                                        Text(status.status,
                                            color = Color.White,
                                            modifier = Modifier
                                                .padding(start = 24.dp, top = 8.dp, end = 24.dp, bottom = 8.dp)
                                        )
                                    }
                                }else{
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                selectedPresenceStatus = status.id
                                                _presenceStatuses = selectPresenceStatus(
                                                    selectedPresenceStatus,
                                                    _presenceStatuses
                                                )
                                                onSelectPresenceType(status.status)
                                            }
                                            .border(
                                                width = 1.dp,
                                                color = Color.Black,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ){
                                        Text(status.status,
                                            modifier = Modifier
                                                .padding(start = 24.dp, top = 8.dp, end = 24.dp, bottom = 8.dp)
                                        )
                                    }
                                }
                                Box(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                    if(selectedPresenceStatus == 3){
                        Box(modifier = Modifier.height(16.dp))
                        ScanDropdownForm(label = "Pilih Izin",
                            placeholder = "--Pilih izin--",
                            items = permitDetails,
                            onChange = { onSelectPermitDetail(it) },
                            onSelectOther = { onSelectOther(it) }
                        )
                    }
                    Box(modifier = Modifier.height(32.dp))
                    if(showConfirmationButton){
                        ButtonForm(label = "Konfirmasi", action = {onConfirm()})
                    }
                }
            }
        }
    }
}

private fun selectPresenceStatus(selectedIndex:Int, items:List<PresenceStatus>): List<PresenceStatus>{
    var copyPresenceStatus = items
    copyPresenceStatus.map{
        it.isActive = it.id == selectedIndex
    }
    return copyPresenceStatus
}

@Composable
fun ErrorPopup(
    isShown: Boolean,
    onClose: () -> Unit,
    onClick: () -> Unit
){
    if(isShown){
        AnimatedVisibility(
            visible = isShown,
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5F))
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1F))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(color = Color.White)
                    .padding(24.dp)
                ) {
                    Row{
                        Box(modifier = Modifier.weight(1F))
                        Icon(
                            Icons.Filled.Close, "close",
                            modifier = Modifier.clickable { onClose() })
                    }
                    Box(modifier = Modifier.height(32.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Image(imageVector = ImageVector.vectorResource(id = R.drawable.ic_failed_tap), contentDescription = "switch")
                    }
                    Box(modifier = Modifier.height(32.dp))
                    Text("Ups, e-KTP Anda tidak dikenal",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Box(modifier = Modifier.height(16.dp))
                    Text("Harap pastikan Anda meletakkan kartu di dekat sensor NFC.\n" +
                            "Atau daftarkan kartu identitas Anda terlebih dahulu.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )
                    Box(modifier = Modifier.height(32.dp))
                    ButtonForm(label = "Hubungi Admin", isOutline = true , action = {onClick()})
                }
            }
        }
    }
}

@Composable
fun LoadingPopup(
    isShown: Boolean
){
    if(isShown){
        AnimatedVisibility(
            visible = isShown,
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5F))
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1F))
                Column(modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(color = Color.White)
                    .padding(24.dp)
                ) {
                    Box(modifier = Modifier.height(32.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Image(imageVector = ImageVector.vectorResource(id = R.drawable.loading_anim_3), "loading")
                    }
                    Box(modifier = Modifier.height(32.dp))
                    Text("Mohon menunggu...",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedLoading(){
    val image = AnimatedImageVector.animatedVectorResource(id = R.drawable.loading_animation)
    var atEnd by remember { mutableStateOf(false) }
    Image(
        painter = rememberAnimatedVectorPainter(animatedImageVector = image, atEnd = atEnd),
        contentDescription = "loading"
    )
}

@Composable
fun ScanDropdownForm(
    label:String,
    placeholder:String,
    items: List<String>,
    onChange: (String) -> Unit,
    onSelectOther: (String) -> Unit
){
    var showOther by remember { mutableStateOf(false) }

    DropdownForm(
        label = label,
        placeholder = placeholder,
        items = items,
        onChange = { index ->
            if(index == 2){
                showOther = true
            }
            onChange(items[index])
        }
    )

    if(showOther){
        ScanTextForm(label = "",
            placeholder = "Mohon sebutkan",
            onChange = {
                onSelectOther(it)
            }
        )
    }
}

@Composable
fun ScanTextForm(label: String, placeholder: String, onChange: (String) -> Unit) {
    var value by remember{ mutableStateOf("") }
    TextForm(
        label = label,
        placeholder = placeholder,
        value = value,
        onChange = {
            value = it
            onChange(it)
        }
    )
}