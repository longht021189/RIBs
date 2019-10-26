package library.rxux.compiler

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import library.rxux.annotation.AutoRouter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

class AutoRouterProcessor : AbstractProcessor() {

    private lateinit var filer: Filer
    private lateinit var messager : Messager

    override fun init(env: ProcessingEnvironment) {
        super.init(env)

        filer = env.filer
        messager  = env.messager
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            AutoRouter::class.java.canonicalName
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(set: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        val dispatcherFile = FileSpec
            .builder(packageName = "library.rxux.core", fileName = "Dispatcher")
            .addType(TypeSpec
                .classBuilder(ClassNames.dispatcherClass)
                .superclass(ClassNames.baseDispatcherClass)
                .addModifiers(KModifier.OPEN)
                .build())
            .build()

        writeToFiler(dispatcherFile)
        return false
    }

    private fun writeToFiler(file: FileSpec) {
        try {
            file.writeTo(filer)
        } catch (error: Throwable) {}
    }
}
