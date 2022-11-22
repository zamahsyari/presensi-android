package or.id.mta.presensi.addevent.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import or.id.mta.presensi.addevent.api.addevent.AddEventApi
import or.id.mta.presensi.addevent.api.addevent.DefaultAddEventApi
import or.id.mta.presensi.addevent.entity.majlis.MajlisEntity
import or.id.mta.presensi.addevent.viewmodel.AddEventViewModel
import or.id.mta.presensi.addevent.viewmodel.AddEventViewModelFactory
import or.id.mta.presensi.common.*
import or.id.mta.presensi.addevent.api.majlis.DefaultMajlisApi
import or.id.mta.presensi.addevent.api.majlis.MajlisApi
import or.id.mta.presensi.addevent.util.TimeIntervalEnum
import or.id.mta.presensi.login.service.AddEventService
import or.id.mta.presensi.login.service.DefaultAddEventService
import or.id.mta.presensi.ui.theme.PrimaryRed
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AddEventScreen(
    client: OkHttpClient = OkHttpClient(),
    token: LiveData<String> = MutableLiveData(""),
    officeId: LiveData<Int> = MutableLiveData(0),
    onSuccess: () -> Unit,
    onBackClick: () -> Unit = {}
){
    val majlisRepository: MajlisApi = DefaultMajlisApi(
        context = LocalContext.current,
        client = client
    )
    val addEventRepository: AddEventApi = DefaultAddEventApi(
        context = LocalContext.current,
        client = client
    )
    val service: AddEventService = DefaultAddEventService(
        majlisRepository = majlisRepository,
        repository = addEventRepository
    )
    val viewModel: AddEventViewModel = viewModel(
        factory = AddEventViewModelFactory(token = token, addEventService = service))
    val errorMessage = viewModel.errorMessage.observeAsState("")
    val coroutineScope = rememberCoroutineScope()

    val dataPerwakilan = viewModel.perwakilanEntities.observeAsState(listOf())
    val dataCabang = viewModel.cabangEntities.observeAsState(emptyList())
    val intervals = TimeIntervalEnum.values().map { it.value }

    val name = viewModel.formName.observeAsState("")
    val description = viewModel.formDescription.observeAsState("")
    val startDate = viewModel.selectedStartDate.observeAsState("2022-01-01")
    val startTime = viewModel.selectedStartTime.observeAsState("09:00")
    val endTime = viewModel.selectedEndTime.observeAsState("12:00")

    LaunchedEffect(Unit){
        if(officeId.value!! == 0){
            viewModel.fetchPerwakilan()
        }else{
            viewModel.setSelectedPerwakilan(officeId.value!!)
            viewModel.setSelectedCabang(officeId.value!!);
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        AddEventHeader(
            onBackClick = {onBackClick()}
        )
        Box(modifier = Modifier.height(32.dp))
        if(!errorMessage.value.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryRed),
                contentAlignment = Alignment.Center
            ){
                Text(errorMessage.value,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
        Box(modifier = Modifier.height(16.dp))
        if(officeId.value!! == 0){
            DropdownForm(
                label = "pilih perwakilan".uppercase(),
                placeholder = "--Pilih perwakilan--",
                items = listMajisToString(dataPerwakilan.value),
                onChange = {index ->
                    val code = getMajlisCodeByIndex(index, dataPerwakilan.value)
                    val majlis = getMajlisByIndex(index, dataPerwakilan.value)
                    val id = getMajlisIdByIndex(index, dataPerwakilan.value)
                    viewModel.setSelectedPerwakilanCode(code)
                    viewModel.setSelectedPerwakilan(id)
                    viewModel.setPerwakilanEntity(majlis)
                    viewModel.fetchCabang()
                }
            )
            Box(modifier = Modifier.height(16.dp))
            DropdownForm(
                label = "pilih cabang".uppercase(),
                placeholder = "--Pilih cabang--",
                items = listMajisToString(dataCabang.value),
                onChange = {index ->
                    val id = getMajlisIdByIndex(index, dataCabang.value)
                    viewModel.setSelectedCabang(id)
                }
            )
            Box(modifier = Modifier.height(16.dp))
        }
        TextForm(
            label = "nama event".uppercase(),
            placeholder = "ketik nama event",
            value = name.value,
            onChange = {viewModel.setName(it)}
        )
        Box(modifier = Modifier.height(16.dp))
        TextAreaForm(
            label = "deskripsi event".uppercase(),
            placeholder = "ketik deskripsi event",
            value = description.value,
            onChange = {viewModel.setDescription(it)}
        )
        Box(modifier = Modifier.height(16.dp))
        Row{
            DateForm(
                label = "dimulai".uppercase(),
                initialValue = startDate.value,
                onChange = {viewModel.setSelectedStartDate(it)},
                modifier = Modifier.weight(0.7F)
            )
        }
        Box(modifier = Modifier.height(16.dp))
        Row{
            TimeForm(
                label = "jam mulai".uppercase(),
                initialValue = startTime.value,
                onChange = {viewModel.setSelectedStartTime(it)},
                modifier = Modifier.weight(0.3F)
            )
            Box(modifier = Modifier.width(8.dp))
            TimeForm(
                label = "jam selesai".uppercase(),
                initialValue = endTime.value,
                onChange = {viewModel.setSelectedEndTime(it)},
                modifier = Modifier.weight(0.3F)
            )
        }
//        Box(modifier = Modifier.height(16.dp))
//        DropdownForm(
//            label = "pilih interval waktu".uppercase(),
//            placeholder = "",
//            items = intervals,
//            onChange = {viewModel.setInterval(intervals.get(it))}
//        )
        Box(modifier = Modifier.height(16.dp))
        CheckForm(
            label = "set status aktif".uppercase(),
            checked = false,
            onChange = {viewModel.setSelectedIsActive(it)}
        )
        Box(modifier = Modifier.height(32.dp))
        ButtonForm(
            label = "simpan",
            action = {
                viewModel.submit(onSuccess = {
                    coroutineScope.launch(Dispatchers.Main) {
                        onSuccess()
                    }
                })
            }
        )
        Box(modifier = Modifier.height(24.dp))
    }
}

fun listMajisToString(dataMajlis:List<MajlisEntity>): List<String>{
    val results = mutableListOf<String>()
    dataMajlis.forEach { majlis ->
        results.add(majlis.name)
    }
    return results
}

fun getMajlisCodeByIndex(index:Int, dataMajlis: List<MajlisEntity>): String{
    val majlis = dataMajlis.get(index)
    return majlis.code
}

fun getMajlisIdByIndex(index:Int, dataMajlis: List<MajlisEntity>): Int{
    val majlis = dataMajlis.get(index)
    return majlis.id
}

fun getMajlisByIndex(index:Int, dataMajlis: List<MajlisEntity>): MajlisEntity{
    val majlis = dataMajlis.get(index)
    return majlis
}

fun formatDecimalToString(value:Int): String {
    if(value < 10){
        return "0" + value
    }else{
        return "" + value
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun AddEventViewPreview(){
    AddEventScreen(onSuccess = {})
}