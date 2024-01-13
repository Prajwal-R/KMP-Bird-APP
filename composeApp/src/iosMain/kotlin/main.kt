import androidx.compose.ui.window.ComposeUIViewController
import com.prajwal.app.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
