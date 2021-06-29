package com.wedream.demo.videoeditor.project.asset

import com.wedream.demo.util.IdUtils
import com.wedream.demo.videoeditor.const.Constants.MIN_ASSET_DURATION
import com.wedream.demo.videoeditor.project.AssetType
import com.wedream.demo.videoeditor.project.ProjectModifyListener
import kotlin.math.abs

open class Asset(
    val id: Long,
    val type: AssetType,
    val fixDuration: Double,
    private var clipStart: Double = 0.0,
    private var clipEnd: Double = fixDuration
) {
    open val duration
        get() = clipEnd - clipStart

    private var modifyListener: ProjectModifyListener? = null

    fun getClipStart(): Double {
        return clipStart
    }

    fun getClipEnd(): Double {
        return clipEnd
    }

    fun setClipStart(start: Double) {
        val start = if (start < 0.0) {
            0.0
        } else {
            start
        }
        if (start != clipStart) {
            clipStart = start
            if (clipEnd - clipStart < MIN_ASSET_DURATION) {
                clipStart = clipEnd - MIN_ASSET_DURATION
            }
            modifyListener?.notifyItemModified(this)
        }
    }

    fun setClipEnd(end: Double) {
        val end = if (end > fixDuration) {
            fixDuration
        } else {
            end
        }
        if (end != clipEnd) {
            clipEnd = end
            if (clipEnd - clipStart < MIN_ASSET_DURATION) {
                clipEnd = clipStart + MIN_ASSET_DURATION
            }
            modifyListener?.notifyItemModified(this)
        }
    }

    fun setModifyListener(listener: ProjectModifyListener) {
        this.modifyListener = listener
    }

    fun removeModifyListener() {
        this.modifyListener = null
    }

    open fun cloneObject(): Asset {
        return Asset(IdUtils.nextId(), type, fixDuration, clipStart, clipEnd)
    }
}