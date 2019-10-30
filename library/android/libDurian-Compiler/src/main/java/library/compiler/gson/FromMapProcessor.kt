package library.compiler.gson

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import library.annotation.gson.FromMap
import library.compiler.core.BaseProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class FromMapProcessor : BaseProcessor() {
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            FromMap::class.java.canonicalName
        )
    }

    override fun process(set: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        val files = ArrayList<FileSpec>()

        for (element in env.getElementsAnnotatedWith(FromMap::class.java)) {
            if (element.kind != ElementKind.CLASS || element !is TypeElement) {
                throw RuntimeException("Only Applied to Class")
            }

            val packageElement = element
                .enclosingElement as PackageElement

            val typeSpec = TypeSpec
                .objectBuilder(ClassName(
                    packageElement.qualifiedName.toString(),
                    simpleNames = listOf("${element.simpleName}Util")))
                .build()

            files.add(FileSpec
                .builder(
                    packageElement.qualifiedName.toString(),
                    fileName = "${element.simpleName}Util.kt")
                .addType(typeSpec)
                .build())

            messager.printMessage(Diagnostic.Kind.WARNING, "======1111> ${element.qualifiedName}")

            element.enclosedElements.forEach {
                messager.printMessage(Diagnostic.Kind.WARNING, "======1> $it, ${it.javaClass}")
            }
            .let {
                messager.printMessage(Diagnostic.Kind.WARNING, "======2> $it, ${it.javaClass}")
            }
            element.typeParameters.forEach {
                messager.printMessage(Diagnostic.Kind.WARNING, "======3> $it, ${it.javaClass}")
            }
        }

        files.forEach { writeToFiler(it) }
        return false
    }
}