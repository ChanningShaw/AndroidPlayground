package com.wedream.demo.app.track

interface IParamsSource {
    fun fillTrackParams(params: TrackParams)
}

interface ITrackNode : IParamsSource {
    /**
     * 返回当前节点的父节点
     */
    fun parentTrackNode(): ITrackNode? {
        return null
    }

    /**
     * 返回当前节点的来源节点
     */
    fun referrerTrackNode(): ITrackNode? {
        return null
    }

    /**
     * 填充当前节点的埋点参数。默认实现为空
     */
    override fun fillTrackParams(params: TrackParams) {

    }
}

interface IPageTrackNode: ITrackNode {
    fun getPageName(): String {
        return this.javaClass.simpleName
    }
}