package com.longforus.mvpautocodeplus.maker

import com.intellij.ide.actions.CreateFileAction
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.ide.fileTemplates.actions.CreateFromTemplateActionBase
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.SmartPointerManager
import com.intellij.util.IncorrectOperationException
import org.apache.velocity.runtime.parser.ParseException

/**
 * Created by XQ Yang on 2018/6/28  15:34.
 * Description : 创建对应的源文件
 */




fun createFileFromTemplate(fileName: String?,
    template: FileTemplate,
    d: PsiDirectory,
    defaultTemplateProperty: String?,
    openFile: Boolean,
    liveTemplateDefaultValues: Map<String, String?>,
    author: String?): PsiFile? {
    var name = fileName
    var dir = d
    if (name != null) {
        val mkdirs = CreateFileAction.MkDirs(name, dir)
        name = mkdirs.newName
        dir = mkdirs.directory
    }

    val project = dir.project
    try {
        val defaultProperties = FileTemplateManager.getInstance(dir.project).defaultProperties
        defaultProperties.putAll(liveTemplateDefaultValues)
        if (!author.isNullOrEmpty()) {
            defaultProperties["USER"] = author
        }
        val psiFile = FileTemplateUtil.createFromTemplate(template, name, defaultProperties, dir)
            .containingFile
        val pointer = SmartPointerManager.getInstance(project).createSmartPsiElementPointer(psiFile)

        val virtualFile = psiFile.virtualFile
        if (virtualFile != null) {
            if (openFile) {
                if (template.isLiveTemplateEnabled) {
                    CreateFromTemplateActionBase.startLiveTemplate(psiFile, liveTemplateDefaultValues)
                } else {
                    FileEditorManager.getInstance(project).openFile(virtualFile, true)
                }
            }
            if (defaultTemplateProperty != null) {
                PropertiesComponent.getInstance(project).setValue(defaultTemplateProperty, template.name)
            }
            return pointer.element
        }
    } catch (e: ParseException) {
        throw IncorrectOperationException("Error parsing Velocity template: " + e.message, e as Throwable)
    } catch (e: IncorrectOperationException) {
        throw e
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}
