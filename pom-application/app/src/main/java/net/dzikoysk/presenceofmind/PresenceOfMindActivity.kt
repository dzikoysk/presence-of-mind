package net.dzikoysk.presenceofmind

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import kotlinx.coroutines.delay
import net.dzikoysk.presenceofmind.pages.Page
import net.dzikoysk.presenceofmind.pages.Router
import kotlin.time.Duration.Companion.minutes

const val DATA_VERSION = "v1.0.0-RC.5"
const val UPDATE_CODE = 0xFADED

class PresenceOfMindActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val presenceOfMind = PresenceOfMind.getInstance(this)

        setTheme(when (presenceOfMind.presenceRepository.isLightMode()) {
            true -> R.style.Theme_LightPresenceOfMind
            false -> R.style.Theme_DarkPresenceOfMind
        })

        runCatching {
            val appUpdateManager = AppUpdateManagerFactory.create(this.baseContext)
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo

            appUpdateInfoTask.addOnSuccessListener {
                when {
                    !it.isUpdateTypeAllowed(FLEXIBLE) -> return@addOnSuccessListener
                    it.updateAvailability() != UPDATE_AVAILABLE -> return@addOnSuccessListener
                    it.availableVersionCode() == presenceOfMind.presenceRepository.getLatestVersionCode() -> return@addOnSuccessListener
                }
                presenceOfMind.presenceRepository.setLatestVersionCode(it.availableVersionCode())
                appUpdateManager.startUpdateFlowForResult(it, FLEXIBLE, this, UPDATE_CODE)
            }
        }

        presenceOfMind.taskService.refreshTasksState()

        setContent {
            LaunchedEffect(Unit) {
                while(true) {
                    delay(1.minutes)
                    presenceOfMind.taskService.refreshTasksState()
                }
            }

            Router(
                presenceRepository = presenceOfMind.presenceRepository,
                taskService = presenceOfMind.taskService,
                restartActivity = {
                    recreate()
                },
                page = Page.DASHBOARD
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UPDATE_CODE) {
            if (resultCode != RESULT_OK) {
                Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        PresenceOfMind.getInstance(this).taskService.refreshTasksState()
        super.onStop()
    }

}