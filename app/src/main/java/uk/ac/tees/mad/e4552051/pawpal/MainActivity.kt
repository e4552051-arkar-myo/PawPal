package uk.ac.tees.mad.e4552051.pawpal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.e4552051.pawpal.data.local.notification.ReminderNotifications
import uk.ac.tees.mad.e4552051.pawpal.ui.navigation.PawPalNavGraph
import uk.ac.tees.mad.e4552051.pawpal.ui.theme.PawPalTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val REQ_NOTI = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Request POST_NOTIFICATIONS on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQ_NOTI
                )
            }
        }

        // 2) Create notification channel
        ReminderNotifications.createChannel(this)

        enableEdgeToEdge()
        setContent {
            PawPalTheme {
                Surface {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()
    PawPalNavGraph(navController = navController)
}