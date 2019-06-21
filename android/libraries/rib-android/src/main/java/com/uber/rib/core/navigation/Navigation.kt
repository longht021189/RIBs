package com.uber.rib.core.navigation

import android.net.Uri
import android.support.v4.util.ArrayMap
import android.support.v4.util.LruCache
import java.io.Closeable
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference
import java.util.*

abstract class Navigation(
    private val maxSizeCache: Int = MAX_SIZE_CACHE_DEFAULT
) : Closeable {
    private var isClosed = false
    private val lruCache by lazy {
        LruCache<String, Node>(maxSizeCache)
    }
    private val nodeMap by lazy {
        ArrayMap<String, WeakReference<Node>>()
    }
    private val managerMap by lazy {
        ArrayMap<String, Manager>()
    }

    protected fun createBackStack(backStackName: String, defaultUri: Uri) {
        if (isClosed) return
        if (managerMap.contains(backStackName)) {
            throw IllegalArgumentException("Back Stack $backStackName Is Exists.")
        }

        managerMap[backStackName] = Manager(backStackName, defaultUri)
    }

    fun getNodeManager(backStackName: String): NodeManager {
        if (isClosed) {
            throw IllegalArgumentException("Navigation Is Closed.")
        }

        return managerMap[backStackName]
            ?: throw IllegalArgumentException("Back Stack $backStackName Is Not Exists.")
    }

    override fun close() {
        if (isClosed) return
        isClosed = true

        managerMap.forEach { it.value.close() }
        managerMap.clear()
    }

//    private fun getNode(name: String): Node? {
//        return nodeMap[name]?.get()
//    }
//    fun openUri(uri: Uri) {
//        val pathSegments = LinkedList<String>()
//
//        uri.pathSegments.reversed().forEach {
//            val node = getNode(it)
//
//            if (node != null) {
//                node.onNavigation(pathSegments)
//                return
//            } else {
//                pathSegments.add(0, it)
//            }
//        }
//    }

    private inner class Manager(
        private val backStackName: String,
        private val defaultUri: Uri
    ) : NodeManager, Closeable {

        override fun addNode(name: String, node: Node) {
            lruCache.put(name, node)
            nodeMap[name] = WeakReference(node)
        }

        override fun removeNode(name: String) {
            lruCache.remove(name)
            nodeMap.remove(name)
        }

        override fun close() {

        }
    }

    companion object {
        private const val MAX_SIZE_CACHE_DEFAULT = 10
    }
}