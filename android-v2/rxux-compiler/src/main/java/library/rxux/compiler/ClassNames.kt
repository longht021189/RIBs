package library.rxux.compiler

import com.squareup.kotlinpoet.ClassName

object ClassNames {
    val baseDispatcherClass by lazy {
        ClassName(
            packageName = "library.rxux.core",
            simpleNames = listOf("BaseDispatcher"))
    }
    val dispatcherClass by lazy {
        ClassName(
            packageName = "library.rxux.core",
            simpleNames = listOf("Dispatcher"))
    }
}