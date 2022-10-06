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

@Composable
fun EventScreen(
    client: OkHttpClient = OkHttpClient(),
    token: LiveData<String> = MutableLiveData(""),
    onSelect: (Int) -> Unit = {},
    onSelectEvent: (EventEntity) -> Unit = {},
    onAddEventClick: () -> Unit = {}
){
    val repository: EventApi = DefaultEventApi(
        context = LocalContext.current,
        client = client
    )
    val service: EventService = DefaultEventService(repository)
    val viewModel: EventViewModel = viewModel(
        factory = EventViewModelFactory(token = token, eventService = service))
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
        EventHeader(onAddEventClick = { onAddEventClick() })
        Box(modifier = Modifier.height(24.dp))
        SearchForm(value = query.value, placeholder = "Cari nama acara...") {
            query.value = it
            viewModel.setQueryAndSearch(it)
        }
        Box(modifier = Modifier.height(24.dp))
        GenerateItems(eventEntities = data.value,
            onClick = {selectedId ->
                coroutineScope.launch(Dispatchers.Main) {
                    onSelect(selectedId)
                }
            },
            onSelectEvent = {selectedEvent ->
                coroutineScope.launch(Dispatchers.Main) {
                    onSelectEvent(selectedEvent)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventScreenPreview(){
    EventScreen()
}