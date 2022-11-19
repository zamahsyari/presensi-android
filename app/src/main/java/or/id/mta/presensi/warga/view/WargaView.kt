package or.id.mta.presensi.warga.view

import SearchForm
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import or.id.mta.presensi.event.api.DefaultEventApi
import or.id.mta.presensi.event.api.EventApi
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.event.viewmodel.EventViewModel
import or.id.mta.presensi.event.viewmodel.EventViewModelFactory
import or.id.mta.presensi.login.service.DefaultEventService
import or.id.mta.presensi.login.service.EventService
import or.id.mta.presensi.scan.api.warga.DefaultWargaApi
import or.id.mta.presensi.scan.api.warga.WargaApi
import or.id.mta.presensi.warga.service.DefaultWargaService
import or.id.mta.presensi.warga.service.WargaService
import or.id.mta.presensi.warga.viewmodel.WargaViewModel
import or.id.mta.presensi.warga.viewmodel.WargaViewModelFactory

@Composable
fun WargaScreen(
    client: OkHttpClient = OkHttpClient(),
    token: LiveData<String> = MutableLiveData(""),
    event: LiveData<EventEntity> = MutableLiveData(),
    officeId: LiveData<Int> = MutableLiveData(0),
    onSelect: (Int) -> Unit = {},
    onBackClick: () -> Unit = {}
){
    val repository: WargaApi = DefaultWargaApi(
        context = LocalContext.current,
        client = client
    )
    val service: WargaService = DefaultWargaService(repository)
    val viewModel: WargaViewModel = viewModel(
        factory = WargaViewModelFactory(
            token = token, officeId = officeId, wargaService = service, event = event)
    )
    val coroutineScope = rememberCoroutineScope()
    val data = viewModel.eventEntities.observeAsState(emptyList())
    var query = remember{ mutableStateOf("") }

    LaunchedEffect(Unit){
        viewModel.fetchData()
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        WargaHeader(onBackClick = {onBackClick()})
        Box(modifier = Modifier.height(24.dp))
        SearchForm(value = query.value, placeholder = "Cari nama warga...") {
            query.value = it
            viewModel.setQueryAndSearch(it)
        }
        Box(modifier = Modifier.height(24.dp))
        GenerateWargaItems(userPresences = data.value, onClick = {selectedId ->
            coroutineScope.launch(Dispatchers.Main) {
                onSelect(selectedId)
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun WargaScreenPreview(){
    WargaScreen()
}