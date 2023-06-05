package org.like.a.fly.models

import android.app.Application
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import org.like.a.fly.models.airsim.AirSimApiWrapper
import org.like.a.fly.models.airsim.ObsfucatorApiWrapper
import java.util.*
import kotlin.concurrent.timerTask

class DroneASim (application: Application) : Drone {
    private val serverApi = ObsfucatorApiWrapper(application)
    private val airSimApi = AirSimApiWrapper()
    private val scope =
        CoroutineScope(Dispatchers.IO + Job())
    override var rcDataProvider : RCDataProvider? = null
            set (value) {
            field = value
            scope.launch {
                value?.rcDataFlow?.collectLatest {
                    updateRcData(it)
                }
            }

        }
    override fun updateRcData(rcData: RCData) {
        scope.launch {
            airSimApi.pitch = rcData.pitch
            airSimApi.roll = rcData.roll
            airSimApi.yaw = rcData.yaw
            airSimApi.throttle = rcData.throttle
        }
    }

    override val environment: DroneEnv = DroneASimEnv(serverApi)
    override fun liftOff() {
        airSimApi.liftOff()
    }

    override fun showLog() {
        serverApi.nextScene()
    }

    override fun nextCameraMode() {
        TODO("Not yet implemented")
    }
    override fun droneStateFlow() : Flow<DroneState> = flow {
        delay(600)
        while (true) {
            emit(airSimApi.getState())
            delay(200)
        }
    }

    val keepAliveTimer: Timer = Timer().apply {
        scheduleAtFixedRate(
            timerTask()
            {
                serverApi.keepAlive()
            }, 5000, 5000)
    }
}