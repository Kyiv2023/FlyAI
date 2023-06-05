package org.like.a.fly.models

import org.like.a.fly.models.airsim.ObsfucatorApiWrapper

open class DroneASimEnv (obsfucatorApiWrapper: ObsfucatorApiWrapper) : DroneEnv {

    val obsfucatorApiWrapper = obsfucatorApiWrapper

    fun switch(to: DroneEnv) {
        if (to == Next) {
            obsfucatorApiWrapper.nextEnv()
        }
    }

    companion object Next : DroneEnv {

    }
}