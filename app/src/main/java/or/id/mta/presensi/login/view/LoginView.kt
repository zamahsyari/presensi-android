package or.id.mta.presensi.login.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import or.id.mta.presensi.common.ButtonForm
import or.id.mta.presensi.common.PasswordForm
import or.id.mta.presensi.common.TextForm
import or.id.mta.presensi.login.api.DefaultLoginApi
import or.id.mta.presensi.login.api.LoginApi
import or.id.mta.presensi.login.service.DefaultLoginService
import or.id.mta.presensi.login.service.LoginService
import or.id.mta.presensi.login.viewmodel.LoginViewModel
import or.id.mta.presensi.login.viewmodel.LoginViewModelFactory
import or.id.mta.presensi.ui.theme.PrimaryGreen
import or.id.mta.presensi.ui.theme.PrimaryRed

@Composable
fun LoginScreen(client: OkHttpClient = OkHttpClient(), onLoggedIn: (String) -> Unit){
    val repository: LoginApi = DefaultLoginApi(
        context = LocalContext.current,
        client = client
    )
    val service: LoginService = DefaultLoginService(repository)
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(loginService = service))
    val errorMessage = viewModel.errorMessage.observeAsState("")
    val successMessage = viewModel.successMessage.observeAsState("")

    var username = remember{ mutableStateOf("") }
    var password = remember{ mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(24.dp),
    ) {
        Box(modifier = Modifier.weight(1F))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = "Presensi",
                fontSize = 100.sp,
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center
            )
        }
        Box(modifier = Modifier.weight(1F))
        Box(modifier = Modifier.height(16.dp))
        if(!successMessage.value.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PrimaryGreen),
                contentAlignment = Alignment.Center
            ){
                Text(successMessage.value,
                    modifier = Modifier.padding(8.dp),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
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
        TextForm(
            label = "username".uppercase(),
            placeholder = "Masukkan username Anda",
            value = username.value,
            onChange = { username.value = it }
        )
        Box(modifier = Modifier.height(16.dp))
        PasswordForm(
            label = "password".uppercase(),
            placeholder = "Masukkan password Anda",
            value = password.value,
            onChange = { password.value = it }
        )
        Box(modifier = Modifier.height(32.dp))
        ButtonForm(label = "submit", action = {
            viewModel.submit(username.value, password.value, onSuccess = {token -> coroutineScope.launch(Dispatchers.Main) {
                onLoggedIn(token)
            }})
        })
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    LoginScreen(onLoggedIn = {})
}