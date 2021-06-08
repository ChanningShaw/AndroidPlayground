package com.wedream.demo.util

object IdUtils {
    /** 开始时间截 (2015-01-01)  */
    private const val startEpoch = 1420041600000L

    /** 上次生成ID的时间截  */
    private var lastTimestamp = -1L

    /** 序列在id中占的位数  */
    private const val sequenceBits = 12L

    /** 毫秒内序列(0~4095)  */
    private var sequence = 0L

    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)  */
    private const val sequenceMask = (-1L shl sequenceBits.toInt()).inv()

    /**
     * 获得下一个ID (该方法是线程安全的)
     * @return SnowflakeId
     */
    @Synchronized
    fun nextId(): Long {
        var timestamp = timeGen()

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw RuntimeException(
                String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp
                )
            )
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = sequence + 1 and sequenceMask
            //毫秒内序列溢出
            if (sequence == 0L) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp)
            }
        } else {
            sequence = 0L
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp

        //移位并通过或运算拼到一起组成64位的ID
        return timestamp - startEpoch shl sequenceBits.toInt() or sequence
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    private fun timeGen(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private fun tilNextMillis(lastTimestamp: Long): Long {
        var timestamp = timeGen()
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen()
        }
        return timestamp
    }
}