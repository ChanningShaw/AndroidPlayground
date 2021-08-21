package com.wedream.demo.reflection.dynamicproxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class SellProxy(val target: IBookSeller) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method, vararg args: Any?): Any {
        val price = method.invoke(target, args) as? Float
        return price ?: 0f + 10f
    }

    fun sellBook(amount: Int): Float {
        val proxy = Proxy.newProxyInstance(
            target.javaClass.classLoader,
            target.javaClass.interfaces,
            this
        ) as IBookSeller
        return proxy.sellBook(amount)
    }
}