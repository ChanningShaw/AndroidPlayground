package com.wedream.demo.app.track

import com.ss.android.ugc.aweme.ecommerce.trackimpl.getDefaultParentTrackNode
import com.ss.android.ugc.aweme.ecommerce.trackimpl.getDefaultPreviousTrackNode

interface ECTrackNode : ITrackNode {

    override fun parentTrackNode(): ITrackNode? = getDefaultParentTrackNode(this)

    override fun previousTrackNode(): ITrackNode? = getDefaultPreviousTrackNode(this)
}

interface ECPageTrackNode : IPageTrackNode {

    override fun parentTrackNode(): ITrackNode? = getDefaultParentTrackNode(this)

    override fun previousTrackNode(): ITrackNode? = getDefaultPreviousTrackNode(this)
}