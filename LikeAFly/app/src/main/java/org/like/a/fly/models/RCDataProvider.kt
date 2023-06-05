package org.like.a.fly.models

import kotlinx.coroutines.flow.Flow

data class RCData(
    var yaw: Float = 0f,
    var pitch: Float = 0f,
    var roll: Float = 0f,
    var throttle: Float = 0f,
    var cameraTilt: Float = 0f,
    var cameraZoom: Float = 0f,
    var payloadReleaseSwitch: Boolean = false,
    var armSwitch: Boolean = false,
    var optSwitch: Boolean = false,
    var opt2Switch: Boolean = false,
) {

}

interface RCDataProvider {
    val rcDataFlow: Flow<RCData>
}