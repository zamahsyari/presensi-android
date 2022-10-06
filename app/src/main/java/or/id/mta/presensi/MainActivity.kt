package or.id.mta.presensi

import EventScreen
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcA
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import okhttp3.OkHttpClient
import or.id.mta.presensi.addevent.view.AddEventScreen
import or.id.mta.presensi.event.entity.EventEntity
import or.id.mta.presensi.login.view.LoginScreen
import or.id.mta.presensi.scan.view.ScanScreen
import or.id.mta.presensi.statistic.view.StatisticScreen
import or.id.mta.presensi.ui.theme.PresensiTheme
import or.id.mta.presensi.warga.view.WargaScreen
import java.nio.charset.Charset

class MainActivity : ComponentActivity() {
    val client = OkHttpClient()
    var apiToken = MutableLiveData("b4ee3f4f-417c-4d0a-a0af-8166d72ae1a1")
    var serialNumber = MutableLiveData("")
    var selectedEventId = MutableLiveData(0)
    var selectedOfficeId = MutableLiveData(0)
    var selectedMemberId = MutableLiveData(0)
    var selectedEvent = MutableLiveData<EventEntity>()

    var ctx: Context? = null
    var adapter: NfcAdapter? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ctx = this;
        adapter = NfcAdapter.getDefaultAdapter(this);
        onNewIntent(this.getIntent());

        setContent {
            PresensiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppNavHost()
                }
            }
        }
    }

    fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent?.getAction()))
        {
            Log.i("NFC READER", "" + intent.toString())
            Log.i("EXTRAS", "" + intent?.extras)
            for (key in intent?.extras?.keySet()!!) {
                Log.d("extras", "$key is a key in the bundle")
            }
            val extraId = intent?.getByteArrayExtra(NfcAdapter.EXTRA_ID)
            val extraIdString= extraId?.toHex()
            Log.i("SERIAL NUMBER", "" + extraIdString)

            val extraTag = intent?.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            IsoDep.get(extraTag)?.let{isoDepTag ->
                Log.i("TAG", "" + isoDepTag.tag.toString())
                val nfcA = NfcA.get(isoDepTag.tag)
                nfcA.connect()
                val s = nfcA.sak
                val a = nfcA.atqa
                val atqa = String(a, Charset.forName("US-ASCII"))
                Log.d("NFCA", "SAK="+s+"\nATQA="+atqa)
                nfcA.close()
            }
            serialNumber.value = "" + extraIdString
        }
    }

    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE)
        val intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))
        adapter?.enableForegroundDispatch(this, pendingIntent, intentFilters, arrayOf(arrayOf("android.nfc.tech.NfcA")))
    }

    override fun onPause() {
        super.onPause()
        if (adapter != null) {
            try {
                adapter!!.disableForegroundDispatch(this)
            } catch (e: NullPointerException) {
                Log.e("NFC", e.message.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Composable
    fun AppNavHost(navController: NavHostController = rememberNavController()){
        val startDestination = "/events"
        NavHost(navController = navController, startDestination = startDestination){
            composable("/login"){
                LoginScreen(
                    client = client,
                    onLoggedIn = {token ->
                        apiToken.postValue(token)
                        navController.navigate("/events")
                    }
                )
            }
            composable("/events"){
                EventScreen(
                    client = client,
                    token = apiToken,
                    onSelect = {eventId ->
                        selectedEventId.postValue(eventId)
//                        navController.navigate("/scan")
                    },
                    onSelectEvent = {eventEntity ->
                        Log.d("EVENT ENTITY", eventEntity.toString())
                        selectedEvent.postValue(eventEntity)
                        navController.navigate("/scan")
                    },
                    onAddEventClick = { navController.navigate("/add-event") }
                )
            }
            composable("/add-event"){
                AddEventScreen(
                    client = client,
                    token = apiToken,
                    onSuccess = { navController.popBackStack() },
                    onBackClick = {navController.popBackStack()}
                )
            }
            composable("/scan"){
                ScanScreen(
                    client = client,
                    token = apiToken,
                    serialNumber = serialNumber,
                    memberId = selectedMemberId,
                    event = selectedEvent,
                    onBackClick = {
                        navController.navigate("/events"){
                            popUpTo("/events")
                        }},
                    onStatsClick = { navController.navigate("/stats") },
                    onScan = {serialNumber.postValue(it)},
                    onAfterScan = {
                        serialNumber.postValue("")
                        selectedMemberId.postValue(0) },
                    onSearchClick = {navController.navigate("/warga")}
                )
            }
            composable("/stats"){
                StatisticScreen(
                    client = client,
                    token = apiToken,
                    eventId = selectedEventId,
                    onBackClick = {navController.popBackStack()}
                )
            }
            composable("/warga"){
                WargaScreen(
                    client = client,
                    token = apiToken,
                    event = selectedEvent,
                    onSelect = {id ->
                        selectedMemberId.postValue(id)
                        navController.navigate("/scan")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }


}