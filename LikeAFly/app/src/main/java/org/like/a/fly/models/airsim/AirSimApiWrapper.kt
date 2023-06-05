package org.like.a.fly.models.airsim

import fr.utbm.airsim.api.MultirotorClientInterface
import fr.utbm.airsim.api.MultirotorState
import fr.utbm.airsim.api.RcData
import kotlinx.coroutines.*
import org.like.a.fly.models.DroneState
import org.like.a.fly.models.KyivLatitude
import org.like.a.fly.models.KyivLongitude
import org.msgpack.rpc.Client
import org.msgpack.rpc.error.RemoteError
import org.msgpack.rpc.loop.EventLoop
import kotlin.random.Random.Default.nextFloat


class AirSimApiWrapper() {
    var pitch: Float = 0f
    var yaw: Float = 0f
    var roll: Float = 0f
    var throttle: Float = 0f

    private val simThread = newSingleThreadContext("airsimThread")
    private val simScope =
        CoroutineScope(simThread  )

    private var manualControl : Job? = null

    private val multirotorClient : MultirotorClientInterface by lazy {
        println("multirotorClient created in:  ${Thread.currentThread().name}")

        val loop = EventLoop.defaultEventLoop()

        val rpcClient = Client("ratss.mooo.com", 41451, loop)
        rpcClient.proxy(MultirotorClientInterface::class.java)
    }
    init {

    }


    fun stopManualControl() {
        manualControl?.cancel()

    }

    fun performManualControl() {
        val secondsOfControl = 1.0f
        manualControl = simScope.launch {
            while (true) {
                multirotorClient.moveByManualAsync(
                    Float.MAX_VALUE,
                    Float.MAX_VALUE,
                    Float.MIN_VALUE,
                    secondsOfControl
                ).join()
                println("moveByManualAsync activated for a second in  ${Thread.currentThread().name}")
                delay(timeMillis = 1)
                withTimeoutOrNull((secondsOfControl * 1000).toLong()) {
                    while (true) {
                        val rcData = RcData(
                            timestamp = 0, // nanos,
                            pitch = pitch,
                            roll = roll,
                            yaw = yaw,
                            throttle = throttle,
                            isInitialized = true,
                            isValid = true
                        )
                        multirotorClient.moveByRCAsync(rcData).join();
                        println("M: P $pitch R $roll Y $yaw T $throttle ${Thread.currentThread().name}")


                        delay(timeMillis = 10)
                    }
                }
                delay(timeMillis = 1)

            }
        }
    }

    private fun fetchState() : DroneState {
        val multirotorState = try {
            multirotorClient.getMultirotorState()
        } catch (e: RemoteError) {
            MultirotorState()
        }
        val geoPoint = multirotorState.gpsLocation
        val kinematicsState = multirotorState.kinematicsStateTrue
        val orientation = kinematicsState.orientation
        val linearVelocity = kinematicsState.linearVelocity
        //todo correct orientation to heading
        val heading = orientation.x
        //todo correct orientation to roll
        val roll = orientation.y
        //todo correct linearVelocity to speed
        val speed = linearVelocity.length()
        return DroneState(
            latitude = geoPoint.latitude.toDouble() + KyivLatitude, //fixme!
            longitude = geoPoint.longitude.toDouble() + KyivLongitude, //fixme!
            altitude = geoPoint.altitude.toDouble(),
            heading = heading.toDouble(),
            speed = speed,
            roll = roll.toDouble(),
            battery = nextFloat(),
        )

    }

    suspend fun getState(): DroneState {

        return withContext(simThread) {
            val droneState = async { fetchState() }
            droneState.await()
        }
    }

    fun liftOff() {
        simScope.launch {
            multirotorClient.reset()
            delay(timeMillis = 100)

//            multirotorClient.confirmConnection()
            multirotorClient.enableApiControl(true)
            delay(timeMillis = 100)

            multirotorClient.armDisarm(true)
            delay(timeMillis = 100)

            multirotorClient.takeoffAsync().join()
            delay(timeMillis = 100)

            multirotorClient.moveToPositionAsync(-10f, 10f, -40f, 5f).join()
            delay(timeMillis = 1000)
            performManualControl()

//            println("API control enabled: ${multirotorClient.isApiControlEnabled()}")

//            val vehicleState = multirotorClient.getMultirotorState()
//            println(vehicleState)

        }
    }
}