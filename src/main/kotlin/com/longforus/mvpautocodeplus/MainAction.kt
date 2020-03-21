package com.longforus.mvpautocodeplus

import com.intellij.featureStatistics.FeatureUsageTracker
import com.intellij.featureStatistics.ProductivityFeatureNames
import com.intellij.ide.actions.CreateFileAction
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.WriteActionAware
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.util.PlatformIcons
import com.longforus.mvpautocodeplus.config.ItemConfigBean
import com.longforus.mvpautocodeplus.maker.*
import com.longforus.mvpautocodeplus.ui.EnterKeywordDialog
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.kotlin.psi.KtFile
import com.intellij.openapi.application.runWriteAction as runWriteAction1


/**
 * Created by XQ Yang on 2018/6/25  13:43.
 * Description :
 */

class MainAction : AnAction("Generate MVP Code", "auto make mvp code", PlatformIcons.CLASS_ICON), WriteActionAware {
    var project: Project? = null
    lateinit var mSelectedState: PropertiesComponent
    fun createFile(enterName: String, templateName: String, dir: PsiDirectory, superImplName: String, contract: PsiFile? = null, fileName: String = enterName): Pair<PsiFile?,
        PsiClass?> {
        var clazz: PsiClass? = null
        val template = TemplateMaker.getTemplate(templateName, project!!) ?: return null to null
        val liveTemplateDefaultValues = TemplateParamFactory.getParam4TemplateName(templateName, enterName, superImplName, contract, mSelectedState)
        val psiFile = createFileFromTemplate(fileName, template, dir, null, false, liveTemplateDefaultValues, mSelectedState.getValue(COMMENT_AUTHOR))
        if (!templateName.contains("Contract")) {
            val openFile = FileEditorManager.getInstance(project!!).openFile(psiFile!!.virtualFile, false)
            val textEditor = openFile[0] as TextEditor

            if (psiFile is PsiJavaFile) {
                if (psiFile.classes.isEmpty()) {
                    return psiFile to null
                }
                clazz = psiFile.classes[0]
            } else if (psiFile is KtFile) {
                if (psiFile.classes.isEmpty()) {
                    return psiFile to null
                }
                clazz = psiFile.classes[0]
            }
            FeatureUsageTracker.getInstance().triggerFeatureUsed(ProductivityFeatureNames.CODEASSISTS_OVERRIDE_IMPLEMENT)
            overrideOrImplementMethods(project!!, textEditor.editor, clazz!!, true)
        }
        return psiFile to clazz
    }

    override fun actionPerformed(e: AnActionEvent) {
        val dataContext = e.dataContext
        val view = LangDataKeys.IDE_VIEW.getData(dataContext) ?: return
        project = CommonDataKeys.PROJECT.getData(dataContext)
        val dir = view.orChooseDirectory

        if (dir == null || project == null) return
//        var state: PropertiesComponent = PropertiesComponent.getInstance(project)
//        if (!state.getBoolean(USE_PROJECT_CONFIG,false)) {
//            state  = PropertiesComponent.getInstance()
//        }
//        if (state.getValue(SUPER_VIEW).isNullOrEmpty()) {
//            Messages.showErrorDialog("Super IView Interface name is null ! $GOTO_SETTING", "Error")
//            return
//        }

        //NewAndroidComponentDialog

        EnterKeywordDialog.getDialog(project) {
            mSelectedState = it.state
            val module = ModuleUtil.findModuleForFile(dir.virtualFile, project!!)
            val facet = AndroidFacet.getInstance(module!!)
            runWriteAction1 {
                if (it.isJava) {
                    doJavaCreate(it, dir, facet)
                } else {
                    doKtCreate(it, dir, facet)
                }
            }
        }
    }

    private fun doKtCreate(it: ItemConfigBean, dir: PsiDirectory, facet: AndroidFacet?) {
        val contractK = createFile(it.name, if (it.generateModel) CONTRACT_TP_NAME_KOTLIN else CONTRACT_TP_NO_MODEL_NAME_KOTLIN, getSubDir(dir, CONTRACT), "",
            fileName = getContractName(it
                .name))

        if (it.vImpl.isNotEmpty() && !it.vImpl.startsWith(IS_NOT_SET)) {
            val sdV = getSubDir(dir, VIEW)
            if (it.isActivity) {
                val activityKt = createFile(it.name, VIEW_IMPL_TP_ACTIVITY_KOTLIN, sdV, it.vImpl, contractK.first, "${it.name}Activity")
                ComponentRegister.registerActivity(project!!,activityKt.second, JavaDirectoryService.getInstance().getPackage(dir), facet!!, "")
                doCreateLayoutFile(it,activityKt.second, project!!, facet, false)
            } else {
                val fragmentKt = createFile(it.name, VIEW_IMPL_TP_FRAGMENT_KOTLIN, sdV, it.vImpl, contractK.first, "${it.name}Fragment")
                doCreateLayoutFile(it,fragmentKt.second, project!!, facet!!, false,false)
            }
        }
        if (it.pImpl.isNotEmpty()) {
            val sdP = getSubDir(dir, PRESENTER)
            createFile(it.name, PRESENTER_IMPL_TP_KOTLIN, sdP, it.pImpl, contractK.first, "${it.name}${TemplateParamFactory.getPresenterOrViewModel(it.pImpl)}")
        }
        if (it.mImpl.isNotEmpty() && it.generateModel) {
            val sdM = getSubDir(dir, MODEL)
            createFile(it.name, MODEL_IMPL_TP_KOTLIN, sdM, it.mImpl, contractK.first, "${it.name}Model")
        }
    }

    private fun doJavaCreate(it: ItemConfigBean, dir: PsiDirectory, facet: AndroidFacet?) {
        val contractJ = createFile(it.name, if (it.generateModel) CONTRACT_TP_NAME_JAVA else CONTRACT_TP_NO_MODEL_NAME_JAVA, getSubDir(dir, CONTRACT),
            "")
        if (it.vImpl.isNotEmpty() && !it.vImpl.startsWith(IS_NOT_SET)) {
            val sdV = getSubDir(dir, VIEW)
            if (it.isActivity) {
                val activityJava = createFile(it.name, VIEW_IMPL_TP_ACTIVITY_JAVA, sdV, it.vImpl, contractJ.first)
                ComponentRegister.registerActivity(project!!,activityJava.second, JavaDirectoryService.getInstance().getPackage(dir), facet!!, "")
                doCreateLayoutFile(it,activityJava.second, project!!, facet, true)
            } else {
                val fragmentJava = createFile(it.name, VIEW_IMPL_TP_FRAGMENT_JAVA, sdV, it.vImpl, contractJ.first)
                doCreateLayoutFile(it,fragmentJava.second, project!!, facet!!, true,false)
            }
        }
        if (it.pImpl.isNotEmpty()) {
            val sdP = getSubDir(dir, PRESENTER)
            createFile(it.name, PRESENTER_IMPL_TP_JAVA, sdP, it.pImpl, contractJ.first)
        }
        if (it.mImpl.isNotEmpty() && it.generateModel) {
            val sdM = getSubDir(dir, MODEL)
            createFile(it.name, MODEL_IMPL_TP_JAVA, sdM, it.mImpl, contractJ.first)
        }
    }


    fun getSubDir(dir: PsiDirectory, dirName: String): PsiDirectory {
        return if (dir.name == CONTRACT) {
            if (dirName == CONTRACT) {
                dir
            } else {
                CreateFileAction.findOrCreateSubdirectory(dir.parentDirectory!!, dirName)
            }
        } else {
            CreateFileAction.findOrCreateSubdirectory(dir, dirName)
        }
    }


    override fun update(e: AnActionEvent) {
        super.update(e)
        val dataContext = e.dataContext
        val presentation = e.presentation

        val enabled = isAvailable(dataContext)

        presentation.isVisible = enabled
        presentation.isEnabled = enabled
    }

    protected fun isAvailable(dataContext: DataContext): Boolean {
        val project = CommonDataKeys.PROJECT.getData(dataContext)
        val view = LangDataKeys.IDE_VIEW.getData(dataContext)
        return project != null && view != null && view.directories.isNotEmpty()
    }

}