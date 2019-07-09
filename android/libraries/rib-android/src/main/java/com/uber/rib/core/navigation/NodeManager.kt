package com.uber.rib.core.navigation

import android.net.Uri

interface NodeManager {
    fun add(path: Uri, data: Any?)
    fun replace(path: Uri, data: Any?, removeDepth: Int = 1)

    fun addNode(name: String, node: Node)
    fun removeNode(name: String)

    fun onBackPressed(): Boolean
    fun saveState()
}