package com.uber.rib.core.navigation

import android.net.Uri
import androidx.collection.ArrayMap
import java.io.Closeable
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashMap

abstract class Navigation(
    private val maxSizeCache: Int = MAX_SIZE_CACHE_DEFAULT
) : Closeable {
    private var isClosed = false

    private val nodeMap by lazy {
        ArrayMap<String, WeakReference<Node>>()
    }
    private val managerMap by lazy {
        ArrayMap<String, NavigationManager>()
    }

    protected fun createBackStack(backStackName: String, defaultUri: Uri, defaultData: Any? = null) {
        if (isClosed) return
        if (managerMap.contains(backStackName)) {
            throw IllegalArgumentException("Back Stack $backStackName Is Exists.")
        }

        managerMap[backStackName] = create(defaultUri, defaultData, backStackName)
    }

    protected open fun create(defaultUri: Uri, defaultData: Any?, backStackName: String): NavigationManager {
        return Manager(defaultUri, defaultData, backStackName)
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

    open fun onAdd(manager: IManager, path: Uri, data: Any? = null) {
        manager.add(path, data)
    }

    open fun onReplace(manager: IManager, path: Uri, removeDepth: Int, data: Any? = null) {
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
        val queryMap: Map<String, String?>? by lazy {
            val names = value.queryParameterNames
            if (names.isNotEmpty()) {
                ArrayMap<String, String?>().apply {
                    names.forEach {
                        put(it, value.getQueryParameter(it))
                    }
                }
            } else {
                null
            }
        }

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
    ) : NavigationManager {

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
                    internalAdd(path, data)
                }
                override fun replace(path: Uri, removeDepth: Int, data: Any?) {
                    internalReplace(path, removeDepth, data)
                }
            }
        }

        override val stackSize: Int get() {
            return uriStack.size
        }

        private fun compare(path1: List<String>, path2: List<String>?) : Boolean {
            if (path1.isEmpty() && path2?.isNotEmpty() != true) return true
            if (path1.size != path2?.size) return false

            path1.forEachIndexed { i, value ->
                if (value != path2[i]) return false
            }

            return true
        }

        private fun onRefresh(prevInfo: UriInfo?, isBack: Boolean) {
            val path = uriStack.peek() ?: return
            var child: String? = null
            var node: Node? = null
            val pathSegments = path.value.pathSegments
            val isSameSegments = compare(pathSegments, prevInfo?.value?.pathSegments)

            run onNavigation@ {
                val size = pathSegments.size

                path.value.pathSegments.forEachIndexed { i, name ->
                    val n = getNode(name)

                    if (n != null && (i + 1 != size || !isSameSegments)) {
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

            if (isBack) {
                val map = HashMap<String, String?>()

                path.queryMap?.forEach { entry ->
                    map[entry.key] = entry.value
                }
                map[BACK_STATE] = BACK_STATE_VALUE

                node?.onNavigation(child, input, map)
            } else {
                node?.onNavigation(child, input, path.queryMap)
            }
        }

        private fun internalAdd(path: Uri, data: Any?) {
            val oldInfo = if (uriStack.empty()) null else uriStack.peek()
            val newInfo = UriInfo(path, data)

            if (oldInfo != null) {
                val name = oldInfo.value.pathSegments.last()
                val node = nodeMap[name]?.get()

                oldInfo.data = node?.onEnterBackStack(oldInfo.data)
            }
            if (newInfo != oldInfo) {
                uriStack.push(newInfo)
                onRefresh(oldInfo, false)
            }
        }

        private fun internalReplace(path: Uri, removeDepth: Int, data: Any?) {
            var depth = removeDepth
            var isOldRemoved = false
            val oldInfo = if (uriStack.empty()) null else uriStack.peek()
            val newInfo = UriInfo(path, data)

            while (!uriStack.empty() && depth != 0) {
                uriStack.pop()
                isOldRemoved = true
                depth--
            }

            if (!isOldRemoved && oldInfo != null) {
                val name = oldInfo.value.pathSegments.last()
                val node = nodeMap[name]?.get()

                oldInfo.data = node?.onEnterBackStack(oldInfo.data)
            }

            when {
                (uriStack.empty() || newInfo != uriStack.peek()) -> {
                    uriStack.push(newInfo)
                    onRefresh(oldInfo, false)
                }
                (isOldRemoved) -> {
                    onRefresh(oldInfo, true)
                }
            }
        }

        override fun getIndex(path: Uri): Int? {
            val size = stackSize
            if (size == 0) return null
            
            for (i in 0 until size) {
                if (uriStack[i].value == path) {
                    return i
                }
            }

            return null
        }

        override fun getPath(): Uri? {
            return if (uriStack.empty()) {
                null
            } else {
                uriStack.peek().value
            }
        }

        override fun getData(): Any? {
            return if (uriStack.empty()) {
                null
            } else {
                uriStack.peek().data
            }
        }

        override fun add(path: Uri, data: Any?) {
            onAdd(iManager, path, data)
        }

        override fun replace(path: Uri, data: Any?, removeDepth: Int) {
            onReplace(iManager, path, removeDepth, data)
        }

        override fun addNode(name: String, node: Node) {
            if (isClosed) return

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

            node.onNavigation(child, input, path.queryMap)
        }

        override fun removeNode(name: String) {
            nodeMap.remove(name)
        }

        override fun onBackPressed(): Boolean {
            return popStack(1)
        }

        override fun popStack(size: Int, force: Boolean): Boolean {
            if (uriStack.size <= size) {
                return false
            }

            if (!force) {
                val lastNode = getNode(uriStack.peek().value.pathSegments.last())
                if (lastNode?.handleBackPress() == true) {
                    return true
                }
            }

            val prev = uriStack.pop()

            if (size > 1) {
                for (i in 0 until (size - 1)) {
                    uriStack.pop()
                }
            }

            onRefresh(prev, true)

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
    
    interface NavigationManager : NodeManager, Closeable

    interface IManager {
        val backStackName: String

        fun add(path: Uri, data: Any?)
        fun replace(path: Uri, removeDepth: Int, data: Any?)
    }

    companion object {
        fun isBack(queryParameters: Map<String, String?>?): Boolean {
            return queryParameters?.get(BACK_STATE) == BACK_STATE_VALUE
        }

        private const val MAX_SIZE_CACHE_DEFAULT = 10
        private const val BACK_STATE = "ABC!@#!@$@#$"
        private const val BACK_STATE_VALUE = "ABC!@#!@$@#_!@#!@#"
    }
}