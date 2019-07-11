package com.uber.rib.core.navigation

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Test
    fun testUpdatePath() {
        val nav = Nav()
        val man = nav.getNodeManager(backStackMain)
        val nodHome = Nod()
        val nodChat = Nod()
        val nodDetail = Nod()

        man.addNode("HOME", nodHome)
        man.addNode("CHAT", nodChat)

        Assert.assertEquals(nodHome.child, "CHAT")
        Assert.assertNull(nodChat.child)

        man.replace(uriDetail, 1)
        man.removeNode("CHAT")
        man.addNode("DETAIL", nodDetail)

        Assert.assertEquals(man.getPath(), uriDetail)
        Assert.assertEquals(nodHome.child, "DETAIL")
        Assert.assertNull(nodDetail.child)

        man.add(uriChat2)
        man.removeNode("DETAIL")
        man.addNode("CHAT", nodChat)

        Assert.assertEquals(man.getPath(), uriChat2)
        Assert.assertEquals(nodHome.child, "CHAT")
        Assert.assertNull(nodChat.child)

        man.onBackPressed()
        man.removeNode("CHAT")
        man.addNode("DETAIL", nodDetail)

        Assert.assertEquals(man.getPath(), uriDetail)
        Assert.assertEquals(man.getData(), 1)
        Assert.assertEquals(nodHome.child, "DETAIL")
        Assert.assertNull(nodDetail.child)
    }

    @Test
    fun testSamePathAndSameQuery() {
        val nav = Nav()
        val man = nav.getNodeManager(backStackMain)
        val nodHome = Nod2()

        man.addNode("HOME", nodHome)
        man.addNode("CHAT", nodHome.chatNode)

        val nodChat = nodHome.chatNode

        man.add(uriChat1)

        Assert.assertEquals(nodHome.chatNode, nodChat)
    }

    @Test
    fun testSamePathAndDiffQuery() {
        val nav = Nav()
        val man = nav.getNodeManager(backStackMain)
        val nodHome = Nod2()

        man.addNode("HOME", nodHome)
        man.addNode("CHAT", nodHome.chatNode)

        val nodChat = nodHome.chatNode

        man.add(uriChat2)

        Assert.assertNotEquals(nodHome.chatNode, nodChat)
    }

    private class Nav : Navigation() {

        init {
            createBackStack(backStackMain, uriChat1)
        }

        override fun create(defaultUri: Uri, defaultData: Any?, backStackName: String): NavigationManager {
            return Man(super.create(defaultUri, defaultData, backStackName))
        }
    }

    private class Man(
        private val origin: Navigation.NavigationManager
    ) : Navigation.NavigationManager {

        override fun getPath(): Uri? {
            return origin.getPath()
        }

        override fun getData(): Any? {
            return origin.getData()
        }

        override fun add(path: Uri, data: Any?) {
            origin.add(path, data)
        }

        override fun replace(path: Uri, data: Any?, removeDepth: Int) {
            origin.replace(path, data, removeDepth)
        }

        override fun addNode(name: String, node: Node) {
            origin.addNode(name, node)
        }

        override fun removeNode(name: String) {
            origin.removeNode(name)
        }

        override fun onBackPressed(): Boolean {
            return origin.onBackPressed()
        }

        override fun saveState() {
            origin.saveState()
        }

        override fun close() {
            origin.close()
        }
    }

    private class Nod : Node {
        var child: String? = null

        override fun onNavigation(
            child: String?, data: Any?,
            queryParameters: Map<String, String?>?
        ) {
            this.child = child
        }

        override fun onEnterBackStack(data: Any?): Any? {
            return data
        }
    }

    private class Nod2 : Node {
        var child: String? = null

        var chatNodeId: String? = null
        lateinit var chatNode: Node

        override fun onNavigation(
            child: String?, data: Any?,
            queryParameters: Map<String, String?>?
        ) {
            this.child = child

            if (chatNodeId != queryParameters?.get("id")) {
                chatNodeId = queryParameters?.get("id")
                chatNode = Nod()
            }
        }

        override fun onEnterBackStack(data: Any?): Any? {
            return data
        }
    }

    companion object {
        private const val backStackMain = "MAIN"
        private val uriChat1 = Uri.parse("/HOME/CHAT?id=1")
        private val uriChat2 = Uri.parse("/HOME/CHAT?id=2")
        private val uriDetail = Uri.parse("/HOME/DETAIL")
    }
}