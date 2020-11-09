package com.wedream.demo.render.gl3

import android.content.res.Resources
import com.wedream.demo.app.ApplicationHolder
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object ResReadUtils {
    /**
     * 读取资源
     *
     * @param resourceId
     * @return
     */
    fun readResource(resourceId: Int): String {
        val builder = StringBuilder()
        try {
            val inputStream: InputStream = ApplicationHolder.instance.resources.openRawResource(resourceId)
            val streamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(streamReader)
            var textLine: String?
            while (bufferedReader.readLine().also { textLine = it } != null) {
                builder.append(textLine)
                builder.append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        return builder.toString()
    }
}