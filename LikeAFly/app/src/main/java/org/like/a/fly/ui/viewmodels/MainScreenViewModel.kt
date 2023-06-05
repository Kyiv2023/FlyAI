package org.like.a.fly.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.like.a.fly.R
import org.like.a.fly.models.*


private val logger = KotlinLogging.logger {}

data class MainScreenUiState  (
    val dummyBackgroundId: Int? = R.drawable.preview_aerial,
    val droneState: DroneState = DroneState(),
    val crosshairSetMode: Boolean = false,
    val controllerVisible: Boolean = false,
    val mapVisible: Boolean = false,
    val mapIsScene: Boolean = true,
    )




class MainScreenViewModel(application: Application, drone:Drone) : AndroidViewModel(application), RCDataProvider {
    private val drone = drone.also {
        it.rcDataProvider = this
    }
    // Holds our view state which the UI collects via [state]
    private val _state = MutableStateFlow(MainScreenUiState())

    private val controllerVisible = MutableStateFlow(false)
    private val mapVisible = MutableStateFlow(false)
    private val mapIsScene = MutableStateFlow(false)
    private val crosshairSetMode = MutableStateFlow(false)

    private var joystickWidgetRCData: RCData = RCData()
    override val rcDataFlow = flow <RCData> {
        //todo implemment, make it emit state when updateJoystickWidgetRCData is called
    }

    val state: StateFlow<MainScreenUiState>
        get() = _state

    init {
        viewModelScope.launch {
            // Combines the latest value from each of the flows, allowing us to generate a
            // view state instance which only contains the latest values.
            combine(
                crosshairSetMode,
                controllerVisible,
                mapVisible,
                mapIsScene,
                drone.droneStateFlow()
            ) { crosshairSetMode, controllerVisible, mapVisible, mapIsScene, droneState ->
                MainScreenUiState(
                    dummyBackgroundId = if (drone is DroneDummy) R.drawable.preview_aerial else null,
                    crosshairSetMode = crosshairSetMode,
                    controllerVisible = controllerVisible,
                    mapVisible = mapVisible,
                    mapIsScene = mapIsScene,
                    droneState = droneState
                )
            }.catch { throwable ->
                // TODO: emit a UI error here. For now we'll just rethrow
                throw throwable
            }.collect {
                _state.value = it
            }
        }

//        refresh(force = false)
    }
//
//    private fun refresh(force: Boolean) {
//        viewModelScope.launch {
//
//        }
//    }





    fun onTBCameraMode() {
        viewModelScope.launch {
            drone.nextCameraMode()
        }
    }


    fun onTBCrossHair() {
        viewModelScope.launch {
        }
    }

    fun onTBMapVisible() {
        viewModelScope.launch {
            mapVisible.value = !mapVisible.value
        }
    }

    fun onTBMapMode() {
        viewModelScope.launch {
            mapIsScene.value = !mapIsScene.value
        }

    }

    fun onTBEnv() {
        viewModelScope.launch {
            drone.environment?.let {
                when (it) {
                    is DroneASimEnv -> it.switch(DroneASimEnv.Next)
                    is DroneDJIEnv -> TODO()
                    else -> {}
                }
            }
        }
    }
    fun onTBLog () {
        viewModelScope.launch {
            drone.showLog()
        }
    }
    fun onTBLiftOff() {
        viewModelScope.launch {
            drone.liftOff()
        }
    }
    fun onTBController() {
        viewModelScope.launch {
            controllerVisible.value = !controllerVisible.value
        }
    }

    private fun updateJoystickWidgetRCData () {
        drone.updateRcData(joystickWidgetRCData)
    }

    fun onLeftJoyMoved(x: Float, y: Float) {
        joystickWidgetRCData.yaw = x
        joystickWidgetRCData.throttle = y
        updateJoystickWidgetRCData()

    }
    fun onRightJoyMoved(x: Float, y: Float) {
        joystickWidgetRCData.pitch = y
        joystickWidgetRCData.roll = x
        updateJoystickWidgetRCData()
    }
}


