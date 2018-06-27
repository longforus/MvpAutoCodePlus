package com.longforus.mvpautocodeplus.maker

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.longforus.mvpautocodeplus.SUPER_MODEL
import com.longforus.mvpautocodeplus.SUPER_PRESENTER
import com.longforus.mvpautocodeplus.SUPER_VIEW
import com.longforus.mvpautocodeplus.config.PersistentState
import com.squareup.javapoet.*
import java.text.SimpleDateFormat
import java.util.*
import javax.lang.model.element.Modifier


/**
 * Created by XQ Yang on 2018/6/26  13:55.
 * Description :
 */
val mDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

fun getViewInfName(name: String) = "I${name}View"
fun getPresenterInfName(name: String) = "I${name}Presenter"
fun getModelInfName(name: String) = "I${name}Model"
fun getContractName(name: String) = "I${name}Contract"

fun make(name: String, type: String, dir: PsiDirectory, project: Project?): PsiFile? {
    return when (type) {
        "1" -> javaNoImpl(name, dir, project)
        else -> null
    }
}

fun javaNoImpl(createName: String, dir: PsiDirectory, project: Project?): PsiFile? {
    val fileName = "${getContractName(createName)}.java"
    val file = dir.findFile(fileName)
    if (file != null) {
        return null
    }
    val path = dir.virtualFile.path
    val packageName = path.substring(path.indexOf("com"), path.length).replace("/", ".")

    val state = ServiceManager.getService(PersistentState::class.java)
    val vClassName = createTypeName(state.getValue(SUPER_VIEW), packageName, createName)
    val pClassName = createTypeName(state.getValue(SUPER_PRESENTER), packageName, createName)
    val mClassName = createTypeName(state.getValue(SUPER_MODEL), packageName, createName)

    val viewType = TypeSpec.interfaceBuilder(getViewInfName(createName)).addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addSuperinterface(vClassName).build()
    val presenterType = TypeSpec.interfaceBuilder(getPresenterInfName(createName)).addModifiers(Modifier
        .PUBLIC, Modifier.STATIC)
        .addSuperinterface(pClassName).build()
    val mType = TypeSpec.interfaceBuilder(getModelInfName(createName)).addModifiers(Modifier.PUBLIC, Modifier.STATIC).addSuperinterface(mClassName).build()
    val contract = TypeSpec.interfaceBuilder(getContractName(createName)).addModifiers(Modifier.PUBLIC).addType(viewType).addType(presenterType)
        .addType(mType)
        .build()
    val javaFile = JavaFile.builder(packageName, contract).addFileComment("This class generate by mvpAutoCodePlus .  Void Young  - " + mDateFormat.format(Date())).build()
    val sb = StringBuilder()
    javaFile.writeTo(sb)
    val result = PsiFileFactory.getInstance(project).createFileFromText(fileName, JavaLanguage.INSTANCE, sb.toString(), true, false, true)
    dir.add(result)
    return result
}


private fun createTypeName(spValue: String?, curPackageName: String, createName: String): TypeName {
    if (spValue.isNullOrEmpty()) {
        throw IllegalArgumentException("Super Interface name is null !")
    }
    val indexOf = spValue!!.lastIndexOf(".")
    var spName = spValue.substring(indexOf + 1, spValue.length)
    var sPPackageName = spValue.substring(0, indexOf)
    var typeName = ""
    if (spName.contains("<")) {
        val endIndex = spName.indexOf("<")
        typeName = spName.substring(endIndex + 1, spName.indexOf(">"))
        spName = spName.substring(0, endIndex)
    }

    var pClassName: TypeName = ClassName.get(sPPackageName, spName)
    val typeClassNames = mutableListOf<ClassName>()
    if (typeName.isNotEmpty()) {
        val types = typeName.split(",")
        for (s in types) {
            typeClassNames.add(when (s) {
                "V" -> ClassName.get(curPackageName, "${getContractName(createName)}.${getViewInfName(createName)}")
                "P" -> ClassName.get(curPackageName, "${getContractName(createName)}.${getPresenterInfName(createName)}")
                "M" -> ClassName.get(curPackageName, "${getContractName(createName)}.${getModelInfName(createName)}")
                else -> throw IllegalArgumentException("$s is not support type")
            })
        }
        if (typeClassNames.isNotEmpty()) {
            pClassName = ParameterizedTypeName.get(pClassName as ClassName, typeClassNames as List<TypeName>?)
        }
    }
    return pClassName
}

