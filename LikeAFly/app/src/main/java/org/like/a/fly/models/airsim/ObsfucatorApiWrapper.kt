package org.like.a.fly.models.airsim

import android.app.Application
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ObsfucatorApiWrapper(application: Application) {
    val queue = Volley.newRequestQueue(application)

    val serverBaseUrl = "http://ratss.mooo.com:8080/obsfucator/"
    fun makeGetRequest(path:String) {
        val url = serverBaseUrl + path
//        logger.debug { "making request to $url" }
        val r = StringRequest(

            Request.Method.GET, url,
            { response ->
//                logger.debug { "received from $url \n $response" }
            },
            {
//                logger.error { "errored out: $it" }

            })

        r.retryPolicy = DefaultRetryPolicy(
            30000,
            0,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        queue.add(r)
    }
    fun nextScene() {
        makeGetRequest("scene/next")
    }

    fun nextEnv () {
        makeGetRequest("airsimEnv/next")
    }

    fun keepAlive () {
        makeGetRequest("keepAlive")

    }


}