package org.like.a.fly.models

import android.location.Location
import kotlinx.coroutines.flow.Flow

val KyivLatitude: Double = 50.45
val KyivLongitude: Double = 30.523333

data class DroneState(
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    val heading: Double = 0.0,
    val altitude: Double = 0.0,
    val speed: Float = 0f,
    val roll: Double = 0.0,
    val pitch: Double = 0.0,
    val battery: Float = 0f,

    ) {


    fun location(): Location {
        val location = Location("DroneState")
        location.longitude = longitude
        location.latitude = latitude
        return location
    }
}

sealed interface Drone {
    //fixme better api is needed
    fun updateRcData(rcData: RCData)
    var rcDataProvider : RCDataProvider?
    fun droneStateFlow() : Flow<DroneState>
    val environment : DroneEnv?
    fun liftOff()
    fun showLog()
    fun nextCameraMode()
}