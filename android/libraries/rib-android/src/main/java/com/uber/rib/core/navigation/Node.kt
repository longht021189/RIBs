package com.uber.rib.core.navigation

interface Node {
    fun onNavigation(child: String?)
}