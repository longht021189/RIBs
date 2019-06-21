package com.uber.rib.core.navigation

interface Node {
    fun onNavigation(pathSegments: List<String>)
}