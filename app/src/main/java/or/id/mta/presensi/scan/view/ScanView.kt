package or.id.mta.presensi.scan.view

import android.util.Log
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import okhttp3.OkHttpClient
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.login.service.ScanService
import or.id.mta.presensi.scan.api.scan.DefaultScanApi
import or.id.mta.presensi.scan.api.scan.ScanApi
import or.id.mta.presensi.scan.api.warga.DefaultWargaApi
import or.id.mta.presensi.scan.api.warga.WargaApi
import or.id.mta.presensi.scan.entity.presence.PresenceStatus
import or.id.mta.presensi.scan.service.DefaultScanService
import or.id.mta.presensi.scan.viewmodel.ScanViewModel
import or.id.mta.presensi.scan.viewmodel.ScanViewModelFactory

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScanScreen(
    client: OkHttpClient = OkHttpClient(),
    token: LiveData<String> = MutableLiveData(""),
    serialNumber: LiveData<String> = MutableLiveData(""),
    memberId: LiveData<Int> = MutableLiveData(0),
    eventId: LiveData<Int> = MutableLiveData(1),
    officeId: LiveData<Int> = MutableLiveData(0),
    event: LiveData<EventEntity>?,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onStatsClick: () -> Unit,
    onScan: (String) -> Unit,
    onAfterScan: () -> Unit
) {
    val context = LocalContext.current
    val repository: ScanApi = DefaultScanApi(
        context = LocalContext.current,
        client = client
    )
    val wargaRepository: WargaApi = DefaultWargaApi(
        context = LocalContext.current,
        client = client
    )
    val service: ScanService = DefaultScanService(
        repository = repository,
        wargaRepository = wargaRepository
    )
    val viewModel: ScanViewModel = viewModel(
        factory = ScanViewModelFactory(context, token, serialNumber, memberId, event!!, service)
    )
    val presenceStatuses = mutableListOf<PresenceStatus>()
    presenceStatuses.add(PresenceStatus(id = 1, status="hadir".uppercase(), isActive = true))
    presenceStatuses.add(PresenceStatus(id = 2, status="sakit".uppercase(), isActive = false))
    presenceStatuses.add(PresenceStatus(id = 3, status="izin".uppercase(), isActive = false))

    val userPresence = viewModel.userPresence.observeAsState()
    val majlisPresence = viewModel.majlisPresence.observeAsState()
    val isLoading = viewModel.isLoading.observeAsState(false)
    val isUserFound = viewModel.isUserFound.observeAsState(false)
    val isUserNotFound = viewModel.isUserNotFound.observeAsState(false)
    val isEnabled = viewModel.isEnabled.observeAsState(false)
    var showConfirmationButton by remember{mutableStateOf(true)}

    var rfidNumber = remember{ mutableStateOf("") }
    val focusRequester = remember{FocusRequester()}

    val keyboardController = LocalSoftwareKeyboardController.current

    if(serialNumber.value!!.isNotEmpty()){
        showConfirmationButton = false
        keyboardController?.hide()
        viewModel.findWarga(true)
        onAfterScan()
    }

    if(memberId.value!! != 0){
        showConfirmationButton = true
        keyboardController?.hide()
        viewModel.findWarga()
        onAfterScan()
    }

    LaunchedEffect(Unit){
        focusRequester.requestFocus()
        keyboardController?.hide()
        viewModel.initEvent()
        viewModel.updateStatistic(event.value!!.id)
    }

    Box{
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {
            ScanHeader(
                onBackClick = { onBackClick() },
                onSearchClick = { onSearchClick() }
            )
            Box(modifier = Modifier.height(16.dp))
            majlisPresence.value?.let { ScanStats(it) }
            Box(modifier = Modifier.weight(weight = 1F))
            ScanImage(onClick = {
//                viewModel.findWarga()
            })
            Box(modifier = Modifier.weight(weight = 1F))
            Box{
                TextField(value = rfidNumber.value, onValueChange = {
                    rfidNumber.value = it
                    Log.d("ONCHANGED", rfidNumber.value)
                }, modifier = Modifier
                    .alpha(0F)
                    .onKeyEvent { keyEvent ->
                        Log.d("KEY EVENT", "" + keyEvent.key.nativeKeyCode)
                        if (keyEvent.key.nativeKeyCode == KEYCODE_ENTER) {
                            onScan(rfidNumber.value)
                            rfidNumber.value = ""
                        }
                        true
                    }
                    .focusRequester(focusRequester)
                )
                ScanControl(onStatsClick = {onStatsClick()},
                    enabled = isEnabled.value,
                    onChange = {viewModel.setEnabled(it)})
            }
//            MyMediaPlayer(context = context)
        }
    }
    LoadingPopup(
        isShown = isLoading.value
    )
    userPresence.value?.let {
        ConfirmationPopup(
        isShown = isUserFound.value,
        showConfirmationButton = showConfirmationButton,
        presenceStatuses = presenceStatuses,
        permitDetails = listOf<String>(
            "kerja".uppercase(),
            "pulang kampung".uppercase(),
            "lain-lain".uppercase()
        ),
        userPresence = it,
        onClose = {viewModel.setUserFound(false)},
        onSelectPresenceType = {viewModel.setPresenceType(it)},
        onSelectPermitDetail = {viewModel.setPermitDetail(it)},
        onSelectOther = {viewModel.setOtherPermission(it)},
        onConfirm = {
            viewModel.submit(onSuccess = {viewModel.setUserFound(false)})
        }
    )
    }
    ErrorPopup(
        isShown = isUserNotFound.value,
        onClose = {viewModel.setUserNotFound(false)},
        onClick = {viewModel.setUserNotFound(false)}
    )
}

@Preview(showBackground = true)
@Composable
fun ScanScreenPreview(){
    ScanScreen(
        event = null,
        onStatsClick = {},
        onBackClick = {},
        onScan = {},
        onAfterScan = {},
        onSearchClick = {})
}