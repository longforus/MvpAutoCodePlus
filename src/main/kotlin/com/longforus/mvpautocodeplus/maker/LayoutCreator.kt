package com.longforus.mvpautocodeplus.maker

import com.android.resources.ResourceFolderType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.codeStyle.JavaCodeStyleManager
import org.jetbrains.android.actions.CreateResourceFileAction
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidResourceUtil

/**
 * @describe
 * @author  longforus
 * @date 2020/3/18  16:07
 */
@Throws(Exception::class)
fun doCreateLayoutFile(element: PsiClass?, directory: PsiDirectory, facet: AndroidFacet, isJava: Boolean): PsiElement? {
    return if (element == null) {
        null
    } else {
        val manifest = Manifest.getMainManifest(facet)
        val appPackage = manifest?.getPackage()?.value
        if (appPackage != null && !appPackage.isEmpty()) {
            ApplicationManager.getApplication().invokeLater {
                createLayoutFileForActivityOrFragment(facet, element, appPackage, directory, isJava)
            }
        }
        element
    }
}

fun createLayoutFileForActivityOrFragment(facet: AndroidFacet, activityClass: PsiClass, appPackage: String, resDirectory: PsiDirectory, isJava: Boolean) {
    if (!facet.isDisposed && activityClass.isValid) {
        val className = activityClass.name
        if (className != null) {
            val layoutFile = CreateResourceFileAction.createFileResource(facet, ResourceFolderType.LAYOUT, null, null, null, true, "Create Layout For '$className'", resDirectory, null, false)
            val layoutFileName = layoutFile?.name
            if (layoutFileName != null) {
                val onCreateMethods = activityClass.findMethodsByName("getLayoutId", false)//todo 生成viewBinding
                if (onCreateMethods.size != 1) {
                    return
                }
                val onCreateMethod = onCreateMethods[0]
                val body = onCreateMethod.body
                if (body != null) {
                    val fieldName = AndroidResourceUtil.getRJavaFieldName(FileUtil.getNameWithoutExtension(layoutFileName))
                    val layoutFieldRef = "$appPackage.R.layout.$fieldName"
                    addInflateStatement(body, layoutFieldRef, isJava)
                }
            }
        }
    }
}


fun addInflateStatement(body: PsiCodeBlock, layoutFieldRef: String, isJava: Boolean) {
    val project = body.project
    val statements = body.statements
    if (statements.size == 1) {
        val statement = statements[0]
        WriteCommandAction.writeCommandAction(project, body.containingFile).run<Throwable> {
            val newStatement = PsiElementFactory.getInstance(project).createStatementFromText(
                "return $layoutFieldRef${if (isJava) ";" else ""}", body)
            statement.replace(newStatement)
            JavaCodeStyleManager.getInstance(project).shortenClassReferences(body)
            CodeStyleManager.getInstance(project).reformat(body)
        }
    }
}

