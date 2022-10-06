package or.id.mta.presensi.statistic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import okhttp3.OkHttpClient
import or.id.mta.presensi.common.StatisticExtension
import or.id.mta.presensi.login.service.ScanService
import or.id.mta.presensi.scan.api.scan.DefaultScanApi
import or.id.mta.presensi.scan.api.scan.ScanApi
import or.id.mta.presensi.scan.api.warga.DefaultWargaApi
import or.id.mta.presensi.scan.api.warga.WargaApi
import or.id.mta.presensi.scan.service.DefaultScanService
import or.id.mta.presensi.scan.viewmodel.ScanViewModel
import or.id.mta.presensi.scan.viewmodel.ScanViewModelFactory
import or.id.mta.presensi.statistic.service.DefaultStatisticService
import or.id.mta.presensi.statistic.service.StatisticService
import or.id.mta.presensi.statistic.util.PresenceStatusEnum
import or.id.mta.presensi.statistic.viewmodel.StatisticViewModel
import or.id.mta.presensi.statistic.viewmodel.StatisticViewModelFactory
import or.id.mta.presensi.ui.theme.PrimaryGrey

@Composable
fun StatisticScreen(
    client: OkHttpClient = OkHttpClient(),
    token: LiveData<String> = MutableLiveData(""),
    eventId: LiveData<Int> = MutableLiveData(1),
    onBackClick: () -> Unit = {}
){
    val repository: ScanApi = DefaultScanApi(
        context = LocalContext.current,
        client = client
    )
    val service: StatisticService = DefaultStatisticService(
        repository = repository
    )
    val viewModel: StatisticViewModel = viewModel(
        factory = StatisticViewModelFactory(token, eventId, service)
    )

    val groupedListWarga = viewModel.groupedListWarga.observeAsState(StatisticExtension.groupAndSort(viewModel.generateListWarga(), "HADIR", 0))
    val totalMale = viewModel.totalMale.observeAsState(0)
    val totalFemale = viewModel.totalFemale.observeAsState(0)
    val cabang = viewModel.cabang.observeAsState("")

    var presence = remember { mutableStateOf(PresenceStatusEnum.HADIR.value) }
    var sortType = remember { mutableStateOf(0) }

    LaunchedEffect(Unit){
        viewModel.fetchData()
    }

    Column{
        Box {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                StatisticHeader(
                    onChange = {
                        sortType.value = it
                        viewModel.fetchData(sortType.value, presence.value, true)
                    },
                    onBackClick = {onBackClick()}
                )
                Box(modifier = Modifier.height(16.dp))
                PresenceDropdown(
                    items = PresenceStatusEnum.values().map { it.value },
                    onChange = {
                        presence.value = it
                        viewModel.fetchData(sortType.value, presence.value, true)
                    }
                )
            }
        }
        StatisticListWarga(groupedListWarga = groupedListWarga.value!!)
//                Box(modifier = Modifier.weight(1F))
        Box(
            modifier = Modifier
                .background(color = PrimaryGrey)
                .padding(24.dp)
        ){
            Row{
                Row{
                    Column{
                        Box(modifier = Modifier.height(8.dp))
                        Text("total".uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(cabang.value,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                Box(modifier = Modifier.weight(1F))
                Row{
                    Column(
                        horizontalAlignment = Alignment.End
                    ){
                        Row{
                            Box(
                                modifier= Modifier
                                    .height(20.dp),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                Text("Pa",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(modifier = Modifier.width(8.dp))
                            Text(""+totalMale.value,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Row{
                            Box(
                                modifier= Modifier
                                    .height(20.dp),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                Text("Pi",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Box(modifier = Modifier.width(8.dp))
                            Text(""+totalFemale.value,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticScreenPreview(){
    StatisticScreen()
}