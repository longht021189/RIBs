package library.compiler.core

import com.squareup.kotlinpoet.FileSpec
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.SourceVersion

abstract class BaseProcessor : AbstractProcessor() {

    protected lateinit var filer: Filer
        private set

    protected lateinit var messager : Messager
        private set

    override fun init(env: ProcessingEnvironment) {
        super.init(env)

        filer = env.filer
        messager  = env.messager
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    protected fun writeToFiler(file: FileSpec) {
        try {
            file.writeTo(filer)
        } catch (error: Throwable) {}
    }
}