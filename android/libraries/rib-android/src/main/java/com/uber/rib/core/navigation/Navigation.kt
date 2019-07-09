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

    protected fun createBackStack(backStackName: String, defaultUri: Uri, defaultData: Any?) {
        if (isClosed) return
        if (managerMap.contains(backStackName)) {
            throw IllegalArgumentException("Back Stack $backStackName Is Exists.")
        }

        managerMap[backStackName] = Manager(defaultUri, defaultData, backStackName)
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

    open fun saveState() {
        managerMap.forEach { entry ->
            entry.value.saveState()
        }
    }

    open fun onBackPressed(): Boolean {
        managerMap.forEach {
            if (it.value.onBackPressed()) {
                return true
            }
        }

        return false
    }

    open fun onAdd(manager: IManager, path: Uri, data: Any?) {
        manager.add(path, data)
    }

    open fun onReplace(manager: IManager, path: Uri, removeDepth: Int, data: Any?) {
        manager.replace(path, removeDepth, data)
    }

    override fun close() {
        if (isClosed) return
        isClosed = true

        managerMap.forEach { it.value.close() }
        managerMap.clear()
    }

    private class UriInfo(
        val value: Uri,
        var data: Any?
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as UriInfo

            if (value != other.value) return false
            if (data != other.data) return false

            return true
        }

        override fun hashCode(): Int {
            var result = value.hashCode()
            result = 31 * result + data.hashCode()
            return result
        }
    }

    private inner class Manager(
        defaultUri: Uri,
        defaultData: Any?,
        private val backStackName: String
    ) : NodeManager, Closeable {

        private val uriStack by lazy {
            val stack = Stack<UriInfo>()
            stack.push(UriInfo(defaultUri, defaultData))
            stack
        }
        private val iManager by lazy {
            object : IManager {
                override val backStackName: String get() {
                    return this@Manager.backStackName
                }
                override fun add(path: Uri, data: Any?) {
                    internalAdd(path, data, false)
                }
                override fun replace(path: Uri, removeDepth: Int, data: Any?) {
                    internalReplace(path, removeDepth, data)
                }
            }
        }

        private fun onRefresh() {
            val path = uriStack.peek() ?: return
            var child: String? = null
            var node: Node? = null

            run onNavigation@ {
                path.value.pathSegments.forEach { name ->
                    val n = getNode(name)
                    if (n != null) {
                        node = n
                    } else {
                        child = name
                        return@onNavigation
                    }
                }
            }

            val input = if (child == null) {
                path.data
            } else {
                null
            }

            node?.onNavigation(child, input)
        }

        private fun internalAdd(path: Uri, data: Any?, isReplace: Boolean) {
            if (!uriStack.empty() && path == uriStack.peek().value) return

            if (!uriStack.empty() && !isReplace) {
                uriStack.peek().apply saveState@{
                    val name = value.pathSegments.last()
                    val node = nodeMap[name]?.get()

                    this.data = node?.onEnterBackStack(data)
                }
            }
            uriStack.push(UriInfo(path, data))

            onRefresh()
        }

        private fun internalReplace(path: Uri, removeDepth: Int, data: Any?) {
            var depth = removeDepth

            while (!uriStack.empty() && depth != 0) {
                uriStack.pop()
                depth--
            }

            internalAdd(path, data, removeDepth != 0)
        }

        override fun add(path: Uri, data: Any?) {
            onAdd(iManager, path, data)
        }

        override fun replace(path: Uri, data: Any?, removeDepth: Int) {
            onReplace(iManager, path, removeDepth, data)
        }

        override fun addNode(name: String, node: Node) {
            if (isClosed) return

            lruCache.put(name, node)
            nodeMap[name] = WeakReference(node)

            val path = uriStack.peek()
            val pathSegments = path.value.pathSegments
            val index = pathSegments.indexOf(name)
            val child = if (index < pathSegments.size - 1) { pathSegments[index + 1] } else { null }
            val input = if (child == null) {
                path.data
            } else {
                null
            }

            node.onNavigation(child, input)
        }

        override fun removeNode(name: String) {
            lruCache.remove(name)
            nodeMap.remove(name)
        }

        override fun onBackPressed(): Boolean {
            if (uriStack.size <= 1) {
                return false
            }

            uriStack.pop()
            onRefresh()

            return true
        }

        override fun saveState() {
            if (uriStack.isEmpty()) return

            val info = uriStack.peek()
            val name = info.value.pathSegments.last()
            val data = nodeMap[name]?.get()?.onEnterBackStack(info.data)

            info.data = data
        }

        override fun close() {}
    }

    interface IManager {
        val backStackName: String

        fun add(path: Uri, data: Any?)
        fun replace(path: Uri, removeDepth: Int, data: Any?)
    }

    companion object {
        private const val MAX_SIZE_CACHE_DEFAULT = 10
    }
}