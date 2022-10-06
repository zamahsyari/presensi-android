package or.id.mta.presensi.addevent.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddEventHeader(onBackClick: () -> Unit){
    Row(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Icon(Icons.Filled.ArrowBack, "back",
            modifier = Modifier.clickable { onBackClick() }
        )
        Box(modifier = Modifier.weight(weight = 1F))
        Text(text = "Tambah Event",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Box(modifier = Modifier.weight(weight = 1F))
    }
}