package org.like.a.fly.models

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import dji.common.error.DJIError
import dji.common.error.DJISDKError
import dji.sdk.base.BaseComponent
import dji.sdk.base.BaseProduct
import dji.sdk.flightcontroller.FlightController
import dji.sdk.products.Aircraft
import dji.sdk.sdkmanager.DJISDKInitEvent
import dji.sdk.sdkmanager.DJISDKManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

object DroneDJI : Drone, RCDataProvider, DJISDKManager.SDKManagerCallback {
    private val scope =
        CoroutineScope(Dispatchers.Default + Job())
    private val TAG = "DroneDJI"

    var product: BaseProduct? = null

    val connectionStatus: MutableLiveData<Boolean> = MutableLiveData(false)


    fun startSdkRegistration(context: Context) {
        @Suppress("DEPRECATION") var abi: String? = Build.CPU_ABI
        //if there are no missing permissions, start SDK registration
        if (abi == "x86") { return }
        scope.launch {
//            DJISDKManager.getInstance().registerApp(context, this@DroneDJI)
        }
    }

    fun getFlightController(): FlightController? {
        return (product as Aircraft?)?.flightController
    }

    override fun onRegister(error: DJIError?) {
        if(error == DJISDKError.REGISTRATION_SUCCESS){
            Log.i(TAG, "Application registered")
            DJISDKManager.getInstance().startConnectionToProduct()
        }else{
            Log.e(TAG, "Registration failed: ${error?.description}")
        }
    }
    override fun onProductDisconnect() {
        Log.d(TAG, "onProductDisconnect")
        product = null
        connectionStatus.postValue(false)
    }
    override fun onProductConnect(baseProduct: BaseProduct?) {
        Log.d(TAG, "onProductConnect newProduct: $baseProduct")
        product = baseProduct
        connectionStatus.postValue(product?.isConnected)
    }

    override fun onComponentChange(
        componentKey: BaseProduct.ComponentKey?,
        oldComponent: BaseComponent?,
        newComponent: BaseComponent?
    ) {
        newComponent?.setComponentListener { isConnected ->
            Log.d(TAG, "onComponentConnectivityChanged: $isConnected")
        }
        Log.d(TAG, "onComponentChange key: $componentKey, oldComponent: $oldComponent, newComponent: $newComponent")
    }

    override fun onProductChanged(baseProduct: BaseProduct?) {
        try {
            product = baseProduct
            connectionStatus.postValue(product?.isConnected)
        } catch (E: Exception) {
            Log.e(TAG, "Product is not an aircraft")
        }
    }

    override fun onInitProcess(djisdkInitEvent: DJISDKInitEvent?, i: Int) {}
    override fun onDatabaseDownloadProgress(l: Long, l1: Long) {}

    init {

    }

    override fun updateRcData(rcData: RCData) {
        TODO("Not yet implemented")
    }

    override var rcDataProvider: RCDataProvider?
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun droneStateFlow(): Flow<DroneState> {
        TODO("Not yet implemented")
    }

    override val environment: DroneEnv?
        get() = TODO("Not yet implemented")

    override fun liftOff() {
        TODO("Not yet implemented")
    }

    override fun showLog() {
        TODO("Not yet implemented")
    }

    override fun nextCameraMode() {
        TODO("Not yet implemented")
    }

    override val rcDataFlow: Flow<RCData>
        get() = TODO("Not yet implemented")

}