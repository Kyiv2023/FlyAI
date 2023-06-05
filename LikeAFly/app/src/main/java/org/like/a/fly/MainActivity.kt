package org.like.a.fly



//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arcgismaps.ApiKey
import com.arcgismaps.ArcGISEnvironment
import dji.sdk.base.BaseProduct
import org.like.a.fly.models.DroneASim
import org.like.a.fly.models.DroneDJI
import org.like.a.fly.models.DroneDummy
import org.like.a.fly.ui.screens.MainScreen
import org.like.a.fly.ui.viewmodels.MainScreenViewModel

class MainActivity : AppCompatActivity() {
    val theLifecycle: Lifecycle
        get() {
            return lifecycle
        }


    /**
     * The following class variables can be called without having to create an instance of the MainActivity class
     */
    companion object {
        private val TAG = "MainActivity"
        private const val FLAG_CONNECTION_CHANGE = "dji_sdk_connection_change"
        private lateinit var mProduct: BaseProduct
        private lateinit var mHandler: Handler //this allows you to send and process Message and Runnable objects associated with a thread's MessageQueue

        //array of permission strings defined in the AndroidManifest, which the app requires and may need to request the user for permission to use.
        private val REQUIRED_PERMISSION_LIST: Array<String> = arrayOf(
            Manifest.permission.VIBRATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
        )
        private const val REQUEST_PERMISSION_CODE = 125 //integer constant used when requesting app permissions
    }

    /**
     * Class Variables
     */
    private val missingPermission = ArrayList<String>()

    /**
     * Creating the Activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "MainActivity created")
        ArcGISEnvironment.apiKey = ApiKey.create("AAPK1a14253a7e6b476c9648312fc1d1b93ePs6mhIbwqHdcng4sIUSwyGJVOn4XPoxlu98b7f-QcRa7gOSe25gRIloKLD4oHMGq")


        //If the Android version running on the user's device is at least Android 6 (Marshmallow) or API level 23, check and request permissions.
        //Android versions below 6 automatically grants access to these permissions, which can be dangerous.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkAndRequestPermissions()
        }

//        setContentView(R.layout.activity_main) //inflates the activity_main layout as the activity's view
        setContent {

            MainScreen(viewModel)
        }
        mHandler = Handler(Looper.getMainLooper()) //initiates a Handler that uses the app's main looper (runs on the UI thread)
    }

    /**
     * Checking and Requesting Permissions
     */
    private fun checkAndRequestPermissions(){
        //For each permission in the REQUIRED_PERMISSION_LIST, if the device has not already granted this permission, add it to the missingPermission list
        for(eachPermission in REQUIRED_PERMISSION_LIST){
            if(ContextCompat.checkSelfPermission(this, eachPermission) != PackageManager.PERMISSION_GRANTED){
                missingPermission.add(eachPermission)
                Log.d(TAG, "missing permission: $eachPermission")
            }
        }
        if(missingPermission.isEmpty()){ //if there are no missing permissions, start SDK registration
            DroneDJI.startSdkRegistration(this)
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ //if there are missing permissions, request the user to grant the permissions
            showToast("Need to grant the permission")
            //Making the permission request. The result is handled in onRequestPermissionsResult()
            ActivityCompat.requestPermissions(this,
                missingPermission.toArray(arrayOfNulls<String>(missingPermission.size)),
                REQUEST_PERMISSION_CODE)
        }
    }


    /**
     * Function used to notify the app of status changes
     */
    private fun notifyStatusChange(){
        mHandler.removeCallbacks(updateRunnable) //removes any pending posts of updateRunnable from the message queue
        mHandler.postDelayed(updateRunnable, 500) //adds a new updateRunnable to the message queue, which is executed 0.5 seconds after
    }

    /**
     * Runnable object (executable command) that sends a broadcast with a specific intent.
     */
    private val updateRunnable: Runnable = Runnable {
        val intent = Intent(FLAG_CONNECTION_CHANGE) //This intent lets the broadcast receiver (itself) know that a connection change has occurred.
        sendBroadcast(intent)
    }

    /**
     * Function displays a toast using provided string parameter
     */
    private fun showToast(text: String){
        val handler = Handler(Looper.getMainLooper())
        handler.post{
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Asynchronous function handles the results of ActivityCompat.requestPermissions()
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode,
            permissions,
            grantResults)
        //For every permission in the missingPermissions list, if the permission is granted, remove it from the list
        if(requestCode == REQUEST_PERMISSION_CODE){
            grantResults.size
            val index = grantResults.size-1
            for(i in index downTo 0){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    missingPermission.remove(permissions[i])
                }
            }
        }
        if(missingPermission.isEmpty()){
            DroneDJI.startSdkRegistration(this)
        }else{
            showToast("Missing Permissions!!")
        }
    }



    private inline fun viewModelFactory(crossinline f: () -> MainScreenViewModel) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = f() as T
        }

    private val viewModel  : MainScreenViewModel by lazy {


        ViewModelProvider(
            this,
            viewModelFactory {
                val useDummyDrone = false
                val drone =  if (useDummyDrone) { DroneDummy(application) } else { DroneASim(application) }
                MainScreenViewModel(
                application = application,
                drone = drone) }
        )[MainScreenViewModel::class.java]
    }

}

