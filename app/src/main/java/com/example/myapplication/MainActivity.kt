package com.example.myapplication

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PermissionsScreen()
        }
    }
}

data class Permission(
    val name: String,
    val manifestName: String,
)

enum class Permissions(
    val permission: Permission,
    val minSdk: Int = Build.VERSION_CODES.BASE
) {
    CameraPermission(Permission("Camera", Manifest.permission.CAMERA)),
    MicrophonePermission(Permission("Microphone", Manifest.permission.RECORD_AUDIO)),
    LocationPermission(Permission("Location", Manifest.permission.ACCESS_COARSE_LOCATION)),
    ContactsPermission(Permission("Contacts", Manifest.permission.READ_CONTACTS)),
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    NotificationsPermission(
        Permission("Notifications", Manifest.permission.POST_NOTIFICATIONS),
        minSdk = Build.VERSION_CODES.TIRAMISU
    );

    companion object {
        fun available(): List<Permissions> =
            entries.filter { Build.VERSION.SDK_INT >= it.minSdk }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun PermissionsScreen() {
    val permissions = remember { Permissions.available() }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        permissions.forEach { item ->
            key(item) {
                val state = rememberPermissionState(item.permission.manifestName)
                MyCard(
                    permission = item.permission,
                    isGranted = state.status.isGranted,
                    onClick = { state.launchPermissionRequest() }
                )
            }
        }
    }
}


@Preview
@Composable
fun MyCard(
    permission: Permission = Permission("example", "example"),
    isGranted: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(2.dp),
        border = BorderStroke(2.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Предоставить доступ к: ", fontSize = 20.sp, color = Color.Gray)
            Text(permission.name)
            Text(if (isGranted) " ✅" else " ❌")
        }
    }
}
