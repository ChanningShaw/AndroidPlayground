package com.wedream.demo.app.track

import androidx.annotation.Keep
import com.wedream.demo.BuildConfig
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

typealias TrackParamsUpdater = TrackParams.() -> Unit

@Keep
class TrackParams : HashMap<String, Any?>(), Serializable {
    companion object {
    }

    fun put(map: Map<String, Any?>): TrackParams {
        for (e in map) {
            put(e.key, e.value)
        }
        return this
    }

    fun put(vararg pairs: Pair<String, Any?>): TrackParams {
        for (pair in pairs) {
            put(pair.first, pair.second)
        }
        return this
    }

    fun putIfNull(key: String, value: Any?): TrackParams {
        if (this[key] == null) {
            put(key, value)
        }
        return this
    }

    private fun internalPut(destMap: MutableMap<String, Any?>, key: String, value: Any?) {
        when (value) {
            null, is String, is Number, is Boolean -> {
                destMap[key] = value
            }
            is JSONObject -> {
                destMap[key] = value.toString()
            }
            is JSONArray -> {
                destMap[key] = value.toString()
            }
            else -> {
                if (BuildConfig.DEBUG) {
                    throw RuntimeException("TrackParams only support JSONObject, JSONArray, String, Boolean and Numbers")
                } else {
                    destMap[key] = value.toString()
                }
            }
        }
    }
}