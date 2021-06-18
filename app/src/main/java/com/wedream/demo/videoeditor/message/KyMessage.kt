package com.wedream.demo.videoeditor.message

class KyMessage private constructor() {
  private var mNext: KyMessage? = null
  private var mInPool = false
  var what = -1
  var arg1: Any? = null
  var arg2: Any? = null
  var arg3: Any? = null
  var whether: Boolean = false
  var argi1 = 0
  var argi2 = 0
  var argi3 = 0
  var argl1 = 0L

  fun recycle() {
    check(!mInPool) { "Already recycled." }
    synchronized(sPoolLock) {
      clear()
      if (sPoolSize < MAX_POOL_SIZE) {
        mNext = sPool
        mInPool = true
        sPool = this
        sPoolSize++
      }
    }
  }

  private fun clear() {
    what = -1
    arg1 = null
    arg2 = null
    arg3 = null
    whether = false
    argi1 = 0
    argi2 = 0
    argi3 = 0
    argl1 = 0L
  }

  companion object {
    private const val MAX_POOL_SIZE = 10
    private var sPool: KyMessage? = null
    private var sPoolSize = 0
    private val sPoolLock = Any()
    fun obtain(): KyMessage {
      synchronized(sPoolLock) {
        return if (sPoolSize > 0) {
          val args = sPool
          sPool = sPool!!.mNext
          args!!.mNext = null
          args.mInPool = false
          sPoolSize--
          args
        } else {
          KyMessage()
        }
      }
    }
  }
}
