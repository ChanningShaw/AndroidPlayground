package com.wedream.demo.app

import com.tencent.mmkv.MMKV

object MySharePreference {
    private const val DEFAULT_SP_NAME = "default"

    fun default(): MMKV {
        return MMKV.mmkvWithID(DEFAULT_SP_NAME, MMKV.MULTI_PROCESS_MODE)
    }
}