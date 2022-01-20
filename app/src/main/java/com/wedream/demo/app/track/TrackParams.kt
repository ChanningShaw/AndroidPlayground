package com.wedream.demo.app.track

import androidx.annotation.Keep
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

typealias TrackParamsUpdater = TrackParams.() -> Unit

@Keep
open class TrackParams : HashMap<String, Any?>(), Serializable {

    fun merge(trackParamsJSON: JSONObject?): TrackParams {
        try {
            if (trackParamsJSON != null && trackParamsJSON.length() > 0) {
                for (key in trackParamsJSON.keys()) {
                    putIfNull(key, trackParamsJSON.get(key))
                }
            }
        } catch (ignore: JSONException) {
        }
        return this
    }

    fun merge(params: Map<String, Any?>): TrackParams {
        for (entry in params) {
            putIfNull(entry.key, entry.value)
        }
        return this
    }

    private fun putIfNull(key: String, value: Any?): TrackParams {
        if (this[key] == null) {
            put(key, value)
        }
        return this
    }
}

