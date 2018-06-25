package com.longforus.mvpautocodeplus

import com.intellij.CommonBundle
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.WriteActionAware
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.util.PlatformIcons
import com.longforus.mvpautocodeplus.ui.CreateFileDialog

/**
 * Created by XQ Yang on 2018/6/25  13:43.
 * Description :
 */

class MainAction : AnAction("main", "auto make mvp code", PlatformIcons.CLASS_ICON), WriteActionAware {

    fun createFile(name: String?, templateName: String?, dir: PsiDirectory?): PsiFile? {
        return dir?.createFile(name!!)
    }

    fun getActionName(directory: PsiDirectory?, newName: String?, templateName: String?): String {
        return "create mvp file"
    }

    private fun buildDialog(project: Project?, directory: PsiDirectory?, builder: CreateFileDialog.Builder?) {
        builder?.setTitle(directory?.name)
    }


//    override fun actionPerformed(e: AnActionEvent?) {
//        val project = e?.getData(PlatformDataKeys.PROJECT)
//        val editor = e?.getData(PlatformDataKeys.EDITOR)
//        val data = e?.getData(PlatformDataKeys.SELECTED_ITEM)
//        val currentEditorFile = PsiUtilBase.getPsiFileInEditor(editor!!,project!!)
//        var currentEditorFileName = currentEditorFile?.getName()
//        EnterKeywordDialog.getDialog {
//            Messages.showMessageDialog(it,e?.dataContext?.toString() ?:"no",null)
//        }
//
//    }

    protected val LOG = Logger.getInstance("#com.intellij.ide.actions.CreateFromTemplateAction")


    override fun actionPerformed(e: AnActionEvent) {
        val dataContext = e.dataContext

        val view = LangDataKeys.IDE_VIEW.getData(dataContext) ?: return

        val project = CommonDataKeys.PROJECT.getData(dataContext)

        val dir = view.orChooseDirectory
        if (dir == null || project == null) return

        val builder = CreateFileDialog.createDialog(project)
        buildDialog(project, dir, builder)
        val selectedTemplateName = Ref.create<String>(null)
        val createdElement = builder.show<PsiFile>(getErrorTitle(), getDefaultTemplateName(dir), object : CreateFileDialog.FileCreator<PsiFile> {

            override fun createFile(name: String, templateName: String): PsiFile? {
                selectedTemplateName.set(templateName)
                return this@MainAction.createFile(name, templateName, dir)
            }

            override fun startInWriteAction(): Boolean {
                return this@MainAction.startInWriteAction()
            }

            override fun getActionName(name: String, templateName: String): String {
                return this@MainAction.getActionName(dir, name, templateName)
            }
        })
        if (createdElement != null) {
            view.selectElement(createdElement)
            postProcess(createdElement, selectedTemplateName.get(), builder.customProperties)
        }
    }

    protected fun postProcess(createdElement: PsiFile, templateName: String, customProperties: Map<String, String>?) {

    }


    protected fun getDefaultTemplateName(dir: PsiDirectory): String? {
        val property = getDefaultTemplateProperty()
        return if (property == null) null else PropertiesComponent.getInstance(dir.project).getValue(property)
    }

    protected fun getDefaultTemplateProperty(): String? {
        return null
    }

    override fun update(e: AnActionEvent?) {
        val dataContext = e!!.dataContext
        val presentation = e.presentation

        val enabled = isAvailable(dataContext)

        presentation.isVisible = enabled
        presentation.isEnabled = enabled
    }

    protected fun isAvailable(dataContext: DataContext): Boolean {
        val project = CommonDataKeys.PROJECT.getData(dataContext)
        val view = LangDataKeys.IDE_VIEW.getData(dataContext)
        return project != null && view != null && view.directories.size != 0
    }


    protected fun getErrorTitle(): String {
        return CommonBundle.getErrorTitle()
    }

    //todo append $END variable to templates?
    fun moveCaretAfterNameIdentifier(createdElement: PsiNameIdentifierOwner) {
        val project = createdElement.project
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        if (editor != null) {
            val virtualFile = createdElement.containingFile.virtualFile
            if (virtualFile != null) {
                if (FileDocumentManager.getInstance().getDocument(virtualFile) === editor.document) {
                    val nameIdentifier = createdElement.nameIdentifier
                    if (nameIdentifier != null) {
                        editor.caretModel.moveToOffset(nameIdentifier.textRange.endOffset)
                    }
                }
            }
        }
    }

}