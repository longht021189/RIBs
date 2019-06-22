package com.uber.rib.core.navigation

import android.net.Uri

interface NodeManager {
    fun add(path: Uri)
    fun replace(path: Uri, removeDepth: Int = 1)

    fun addNode(name: String, node: Node)
    fun removeNode(name: String)
}