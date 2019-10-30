package library.compiler.redux

import library.annotation.redux.ToStore
import library.compiler.core.BaseProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

class ToStoreProcessor : BaseProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            ToStore::class.java.canonicalName
        )
    }

    override fun process(set: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        for (element in env.getElementsAnnotatedWith(ToStore::class.java)) {
            if (element.kind != ElementKind.CLASS || element !is TypeElement) {
                throw RuntimeException("Only Applied to Class")
            }

            val superDeclaredType = element.superclass as DeclaredType
            val superTypeElement = superDeclaredType.asElement() as TypeElement

            if (!superTypeElement.qualifiedName.contentEquals("library.core.redux.Reducer")) {
                throw RuntimeException("${element.qualifiedName} isn't Reducer")
            }

            var actionTypeElementValue: TypeElement? = null
            var stateTypeElementValue: TypeElement? = null

            for (item in superDeclaredType.typeArguments) {
                when (item) {
                    is DeclaredType -> {
                        if (actionTypeElementValue == null) {
                            actionTypeElementValue = item.asElement() as TypeElement
                        } else if (stateTypeElementValue == null) {
                            stateTypeElementValue = item.asElement() as TypeElement
                        }
                    }
                    else -> {
                        throw RuntimeException("${element.qualifiedName} Don't Support to " +
                                "Generic Reducer Params Type: ${item.javaClass.canonicalName}")
                    }
                }
            }

            val actionTypeElement = actionTypeElementValue
                ?: throw RuntimeException("Don't Find Action Type in ${element.qualifiedName}")

            val stateTypeElement = stateTypeElementValue
                ?: throw RuntimeException("Don't Find State Type in ${element.qualifiedName}")

            element.enclosedElements.forEach {
                messager.printMessage(Diagnostic.Kind.WARNING, "======4> $it, ${it.javaClass}")
            }
            element.enclosingElement.let {
                messager.printMessage(Diagnostic.Kind.WARNING, "======5> $it, ${it.javaClass}")
            }
            element.typeParameters.forEach {
                messager.printMessage(Diagnostic.Kind.WARNING, "======6> $it, ${it.javaClass}")
            }
        }

        return false
    }

    /*
val dispatcherFile = FileSpec
            .builder(packageName = "library.rxux.core", fileName = "Dispatcher")
            .addType(TypeSpec
                .classBuilder(ClassNames.dispatcherClass)
                .superclass(ClassNames.baseDispatcherClass)
                .addModifiers(KModifier.OPEN)
                .build())
            .build()

        writeToFiler(dispatcherFile)
    */
}