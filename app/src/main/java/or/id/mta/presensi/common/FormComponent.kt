package or.id.mta.presensi.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import or.id.mta.presensi.ui.theme.AlternateGreen
import or.id.mta.presensi.ui.theme.PrimaryBlack
import or.id.mta.presensi.ui.theme.PrimaryGreen
import or.id.mta.presensi.ui.theme.PrimaryGrey
import java.util.*
import kotlin.math.min

@Composable
fun TextForm(
    label: String,
    placeholder: String,
    value: String,
    onChange: (value: String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {
    Column{
        Text(text = label, fontSize = 12.sp, color = PrimaryBlack)
        Box(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = { onChange(it) },
            placeholder = { Text(text = placeholder) },
            keyboardOptions = keyboardOptions,
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
    }
}

@Composable
fun TextAreaForm(
    label: String,
    placeholder: String,
    value: String,
    onChange: (value: String) -> Unit
) {
    Column{
        Text(text = label, fontSize = 12.sp, color = PrimaryBlack)
        Box(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = { onChange(it) },
            placeholder = { Text(text = placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp)
                .height(136.dp)
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
    }
}

@Composable
fun EmailForm(label: String,
              placeholder: String,
              value: String,
              onChange: (value: String) -> Unit
) {
    Column{
        Text(text = label, fontSize = 12.sp, color = PrimaryBlack)
        Box(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = { onChange(it) },
            placeholder = {Text(text = placeholder)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
    }
}

@Composable
fun PasswordForm(label: String,
                 placeholder: String,
                 value: String,
                 onChange: (value: String) -> Unit
) {
    Column{
        Text(text = label, fontSize = 12.sp, color = PrimaryBlack)
        Box(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { onChange(it) },
            placeholder = {Text(text = placeholder)},
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
    }
}

@Composable
fun DropdownForm(
    label:String,
    placeholder:String,
    items: List<String>,
    onChange: (index: Int) -> Unit
){
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Column{
        Text(text = label, fontSize = 12.sp, color = PrimaryBlack)
        Box(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, color = PrimaryGrey, RoundedCornerShape(8.dp)),
        ){
            Row(
                modifier = Modifier.clickable {
                    expanded = true
                }
            ){
                if(items.isEmpty()){
                    Text(placeholder,
                        modifier = Modifier
                            .padding(12.dp),
                        color = PrimaryGrey
                    )
                }else{
                    if(selectedIndex >= items.size){
                        selectedIndex = 0
                        Text(items[0],
                            modifier = Modifier
                                .padding(12.dp)
                        )
                    }else{
                        Text(items[selectedIndex],
                            modifier = Modifier
                                .padding(12.dp)
                        )
                    }
                }
                Box(modifier = Modifier.weight(1F))
                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ){
                    Icon(Icons.Filled.KeyboardArrowDown, "down")
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
                        onChange(selectedIndex)
                    }) {
                        Text(item)
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonForm(label: String, isOutline: Boolean = false, action: () -> Unit){
    if(isOutline){
        Button(
            onClick = action,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, color = AlternateGreen, RoundedCornerShape(8.dp))
        ) {
            Text(
                text = label.uppercase(),
                color = PrimaryGreen,
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp
            )
        }
    }else{
        Button(
            onClick = action,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PrimaryGreen
            ),
            modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, color = AlternateGreen, RoundedCornerShape(8.dp))
        ) {
            Text(
                text = label.uppercase(),
                color = Color.White,
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp
            )
        }
    }
}

fun formatDecimalToString(value:Int): String {
    if(value < 10){
        return "0" + value
    }else{
        return "" + value
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DateForm(
    label: String,
    initialValue: String,
    onChange: (value: String) -> Unit,
    modifier: Modifier = Modifier
){
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedValue by remember { mutableStateOf(initialValue) }
    val datePickerDialog = DatePickerDialog(LocalContext.current, { _, year, month, day ->
        onChange("$year-${formatDecimalToString(month+1)}-${formatDecimalToString(day)}")
        selectedValue = "$year-${formatDecimalToString(month+1)}-${formatDecimalToString(day)}"
    }, year, month, day)

    Column(
        modifier = modifier
    ){
        Text(text = label, fontSize = 12.sp, color = PrimaryBlack)
        Box(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, color = PrimaryGrey, RoundedCornerShape(8.dp)),
        ){
            Row{
                Text(selectedValue,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable {
                            datePickerDialog.show()
                        }
                )
                Box(modifier = Modifier.weight(1F))
                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ){
                    Icon(Icons.Filled.DateRange, "calendar")
                }
            }
        }
    }
}

@Composable
fun TimeForm(
    label:String,
    initialValue:String,
    onChange: (value: String) -> Unit,
    modifier: Modifier = Modifier
){
    val hour: Int
    val minute: Int

    val calendar = Calendar.getInstance()
    hour = calendar.get(Calendar.HOUR)
    minute = calendar.get(Calendar.MINUTE)

    var selectedValue by remember { mutableStateOf(initialValue) }
    val timePickerDialog = TimePickerDialog(LocalContext.current, { _, hour, minute ->
        onChange("${formatDecimalToString(hour)}:${formatDecimalToString(minute)}")
        selectedValue = "${formatDecimalToString(hour)}:${formatDecimalToString(minute)}"
    }, hour, minute, true)

    Column(
        modifier = modifier
    ){
        Text(text = label, fontSize = 12.sp, color = PrimaryBlack)
        Box(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, color = PrimaryGrey, RoundedCornerShape(8.dp)),
        ){
            Row{
                Text(selectedValue,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable {
                            timePickerDialog.show()
                        }
                )
                Box(modifier = Modifier.weight(1F))
                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = ImageVector.vectorResource(
                            id = or.id.mta.presensi.R.drawable.ic_clock
                        ), "clock"
                    )
                }
            }
        }
    }
}

@Composable
fun CheckForm(
    label: String,
    checked: Boolean,
    onChange: (value: Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    val isChecked = remember { mutableStateOf(checked) }
    Column(
        modifier = modifier
    ){
        Row{
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = { isChecked.value = it; onChange(it) }
            )
            Box(
                modifier = Modifier.height(48.dp),
                contentAlignment = Alignment.Center
            ){
                Text(text = label, fontSize = 12.sp, color = PrimaryBlack)
            }
        }
    }
}