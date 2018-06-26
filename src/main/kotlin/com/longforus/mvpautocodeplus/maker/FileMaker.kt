package com.longforus.mvpautocodeplus.maker

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.longforus.mvpautocodeplus.config.PersistentState
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
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

fun javaNoImpl(name: String, dir: PsiDirectory, project: Project?): PsiFile? {
    val fileName = "${getContractName(name)}.java"
    val file = dir.findFile(fileName)
    if (file != null) {
        return null
    }
    val state = ServiceManager.getService(PersistentState::class.java)
    TODO("修改实现细节")
//    val superV = Class.forName(state.getValue(SUPER_VIEW),true,project?.javaClass?.classLoader)
//    val superP = ParameterizedTypeName.get(ClassName.get(Class.forName(state.getValue(SUPER_PRESENTER))), TypeName.get(Class.forName(getViewInfName(name))))
    val superP = ParameterizedTypeName.get(ClassName.get("com.fec.core.inf", "IPresenter"), ClassName.get("com.fec.core.inf", "IPresenter"))
//    val superM = Class.forName(state.getValue(SUPER_MODEL))
    val viewType = TypeSpec.interfaceBuilder(getViewInfName(name)).addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addSuperinterface(ClassName.get("com.fec.core.inf", "IView")).build()
    val presenterType = TypeSpec.interfaceBuilder(getPresenterInfName(name)).addModifiers(Modifier
        .PUBLIC, Modifier.STATIC)
        .addSuperinterface(superP).build()
    val mType = TypeSpec.interfaceBuilder(getModelInfName(name)).addModifiers(Modifier.PUBLIC, Modifier.STATIC).addSuperinterface(
        ClassName.get("com.fec.core.inf", "IModel")).build()
    val contract = TypeSpec.interfaceBuilder(name).addModifiers(Modifier.PUBLIC).addType(viewType).addType(presenterType)
        .addType(mType)
        .build()
    val javaFile = JavaFile.builder("com.fec.core", contract).addFileComment("This class generate by mvpAutoCodePlus .  - " + mDateFormat.format(Date())).build()
    val sb = StringBuilder()
    javaFile.writeTo(sb)
    val result = PsiFileFactory.getInstance(project).createFileFromText(fileName, JavaLanguage.INSTANCE, sb.toString(), true, false, true)
    dir.add(result)
    return result
}