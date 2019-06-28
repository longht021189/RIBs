package com.uber.rib.core.navigation

import android.net.Uri
import androidx.collection.ArrayMap
import androidx.collection.LruCache
import java.io.Closeable
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

        managerMap[backStackName] = Manager(defaultUri)
    }

    fun getNodeManager(backStackName: String): NodeManager {
        if (isClosed) {
            throw IllegalArgumentException("Navigation Is Closed.")
        }

        return managerMap[backStackName]
            ?: throw IllegalArgumentException("Back Stack $backStackName Is Not Exists.")
    }

    private fun getNode(name: String): Node? {
        return nodeMap[name]?.get()
    }

    open fun onBackPressed(): Boolean {
        managerMap.forEach {
            if (it.value.onBackPressed()) {
                return true
            }
        }

        return false
    }

    override fun close() {
        if (isClosed) return
        isClosed = true

        managerMap.forEach { it.value.close() }
        managerMap.clear()
    }

    private inner class Manager(defaultUri: Uri) : NodeManager, Closeable {

        private val uriStack by lazy {
            val stack = Stack<Uri>()
            stack.push(defaultUri)
            stack
        }

        private fun onRefresh() {
            val path = uriStack.peek() ?: return
            var child: String? = null
            var node: Node? = null

            run onNavigation@ {
                path.pathSegments.forEach { name ->
                    val n = getNode(name)
                    if (n != null) {
                        node = n
                    } else {
                        child = name
                        return@onNavigation
                    }
                }
            }

            node?.onNavigation(child)
        }

        override fun add(path: Uri) {
            if (!uriStack.empty() && path == uriStack.peek()) return

            uriStack.push(path)

            onRefresh()
        }

        override fun replace(path: Uri, removeDepth: Int) {
            var depth = removeDepth

            while (!uriStack.empty() && depth != 0) {
                uriStack.pop()
                depth--
            }

            add(path)
        }

        override fun addNode(name: String, node: Node) {
            if (isClosed) return

            lruCache.put(name, node)
            nodeMap[name] = WeakReference(node)

            val pathSegments = uriStack.peek().pathSegments
            val index = pathSegments.indexOf(name)

            node.onNavigation(if (index < pathSegments.size - 1) { pathSegments[index + 1] } else { null })
        }

        override fun removeNode(name: String) {
            lruCache.remove(name)
            nodeMap.remove(name)
        }

        fun onBackPressed(): Boolean {
            if (uriStack.size <= 1) {
                return false
            }

            uriStack.pop()
            onRefresh()

            return true
        }

        override fun close() {}
    }

    companion object {
        private const val MAX_SIZE_CACHE_DEFAULT = 10
    }
}