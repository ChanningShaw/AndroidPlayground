package com.wedream.demo.app.track

interface IParamsSource {
    fun fillTrackParams(params: TrackParams)
}

interface ITrackNode : IParamsSource {

    fun parentTrackNode(): ITrackNode?

    fun previousTrackNode(): ITrackNode?

    override fun fillTrackParams(params: TrackParams) {

    }
}

interface IPageTrackNode : ITrackNode {

    fun getPageNameKey(): String = "page_name"

    fun getPageName(): String = this.javaClass.simpleName

    fun getMapRule(): Map<String, String> = hashMapOf("page_name" to "from_page")
}