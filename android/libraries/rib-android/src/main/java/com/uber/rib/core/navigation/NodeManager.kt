package com.uber.rib.core.navigation

interface NodeManager {
    fun addNode(name: String, node: Node)
    fun removeNode(name: String)
}