package com.wedream.demo.reflection.dynamicproxy

class BookFactory : IBookSeller {
    override fun sellBook(amount: Int): Float {
        return 80f
    }

    override fun print() {
        "我是印刷厂"
    }
}