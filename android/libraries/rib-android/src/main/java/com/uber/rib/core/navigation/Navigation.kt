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

        managerMap[backStackName] = Manager(defaultUri, backStackName)
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

    open fun onAdd(manager: IManager, path: Uri) {
        manager.add(path)
    }

    open fun onReplace(manager: IManager, path: Uri, removeDepth: Int) {
        manager.replace(path, removeDepth)
    }

    override fun close() {
        if (isClosed) return
        isClosed = true

        managerMap.forEach { it.value.close() }
        managerMap.clear()
    }

    private inner class Manager(
        defaultUri: Uri,
        private val backStackName: String
    ) : NodeManager, Closeable {

        private val uriStack by lazy {
            val stack = Stack<Uri>()
            stack.push(defaultUri)
            stack
        }
        private val iManager by lazy {
            object : IManager {
                override val backStackName: String get() {
                    return this@Manager.backStackName
                }
                override fun add(path: Uri) {
                    internalAdd(path)
                }
                override fun replace(path: Uri, removeDepth: Int) {
                    internalReplace(path, removeDepth)
                }
            }
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

            val input = if (child == null) {
                val names = path.queryParameterNames
                if (names.size > 0) {
                    ArrayMap<String, String>().apply {
                        names.forEach {
                            put(it, path.getQueryParameter(it)!!)
                        }
                    }
                } else {
                    null
                }
            } else {
                null
            }

            node?.onNavigation(child, input)
        }

        private fun internalAdd(path: Uri) {
            if (!uriStack.empty() && path == uriStack.peek()) return

            uriStack.push(path)

            onRefresh()
        }

        private fun internalReplace(path: Uri, removeDepth: Int) {
            var depth = removeDepth

            while (!uriStack.empty() && depth != 0) {
                uriStack.pop()
                depth--
            }

            internalAdd(path)
        }

        override fun add(path: Uri) {
            onAdd(iManager, path)
        }

        override fun replace(path: Uri, removeDepth: Int) {
            onReplace(iManager, path, removeDepth)
        }

        override fun addNode(name: String, node: Node) {
            if (isClosed) return

            lruCache.put(name, node)
            nodeMap[name] = WeakReference(node)

            val path = uriStack.peek()
            val pathSegments = path.pathSegments
            val index = pathSegments.indexOf(name)
            val child = if (index < pathSegments.size - 1) { pathSegments[index + 1] } else { null }
            val input = if (child == null) {
                val names = path.queryParameterNames
                if (names.size > 0) {
                    ArrayMap<String, String>().apply {
                        names.forEach {
                            put(it, path.getQueryParameter(it)!!)
                        }
                    }
                } else {
                    null
                }
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

        override fun close() {}
    }

    interface IManager {
        val backStackName: String

        fun add(path: Uri)
        fun replace(path: Uri, removeDepth: Int)
    }

    companion object {
        private const val MAX_SIZE_CACHE_DEFAULT = 10
    }
}