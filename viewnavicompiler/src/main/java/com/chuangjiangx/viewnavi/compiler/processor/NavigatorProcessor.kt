package com.chuangjiangx.viewnavi.compiler.processor

import com.chuangjiangx.viewnavi.compiler.annotation.Navigator
import com.chuangjiangx.viewnavi.compiler.info.NavigatorInfo
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class NavigatorProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val sets = HashSet<String>()

        sets.add(Navigator::class.java.canonicalName)

        return sets
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        val set = roundEnv?.getElementsAnnotatedWith(Navigator::class.java)
        if (null == annotations || annotations.isEmpty() || null == set || set.isEmpty()) {
            return false
        }

        val list = ArrayList<NavigatorInfo>()

        set.forEach {
            if (it.kind == ElementKind.CLASS) {
                val navigator = it.getAnnotation(Navigator::class.java)
                val path = navigator.path
                val mode = navigator.startMode
                val clsName = (it as TypeElement).qualifiedName.toString()

                list.add(NavigatorInfo(path, clsName, mode))
            }
        }

        generate(list)

        return true
    }

    /**
     * 生成代码
     */
    private fun generate(list: ArrayList<NavigatorInfo>) {
        val pkgName = "com.chuangjiangx.viewnavi"
        val clsName = "ViewPaths"

        val navigatorInfo = ClassName("com.chuangjiangx.viewnavi.compiler.info", "NavigatorInfo")
        val array = ClassName("kotlin.collections", "ArrayList")
        val listType = array.parameterizedBy(navigatorInfo)

        val builder = FunSpec
            .builder("getPaths")
            .addAnnotation(JvmStatic::class)
            .returns(listType)
            .addStatement("val list = %T()", listType)

        list.forEach {
            builder.addStatement(
                "list.add( %T(%S, %S, ${it.startMode}) )",
                NavigatorInfo::class,
                it.path,
                it.clsName
            )
        }

        val getPathMap = builder
            .addStatement("return list")
            .build()

        val file = FileSpec
            .builder(pkgName, clsName)
            .addType(
                TypeSpec.objectBuilder("ViewPaths")
                    .addFunction(getPathMap)
                    .build()
            )
            .build()

        file.writeTo(processingEnv.filer)
    }

}