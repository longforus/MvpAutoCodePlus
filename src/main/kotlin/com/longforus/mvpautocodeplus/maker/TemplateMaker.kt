package com.longforus.mvpautocodeplus.maker

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.ide.fileTemplates.impl.FileTemplateManagerImpl
import com.intellij.openapi.project.Project
import com.longforus.mvpautocodeplus.*

/**
 * Created by XQ Yang on 2018/6/28  10:46.
 * Description : template管理类
 */

object TemplateMaker {

    var tpManager: FileTemplateManagerImpl? = null
    val cacheTemplate = HashMap<String, FileTemplate>()


    private fun createContractTemplate(name: String, type: String, content: String) {
        val template = FileTemplateUtil.createTemplate(name, type, content,
            tpManager!!.getTemplates(FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY))
        template.isLiveTemplateEnabled = false
        //保存到ide中,这里就不保存了
//        tpManager.setTemplates(FileTemplateManager.DEFAULT_TEMPLATES_CATEGORY, listOf(template))
        cacheTemplate[name] = template
    }

    fun getTemplate(templateName: String, project: Project): FileTemplate? {
        if (cacheTemplate.contains(templateName)) {
            return cacheTemplate[templateName] as FileTemplate
        } else if (tpManager == null) {
            tpManager = FileTemplateManagerImpl.getInstanceImpl(project)
        }

        when (templateName) {
            CONTRACT_TP_NAME_JAVA -> createContractTemplate(templateName, "java", TemplateCons.CONTRACT_TP_CONTENT_JAVA)
            CONTRACT_TP_NAME_KOTLIN -> createContractTemplate(templateName, "kt", TemplateCons.CONTRACT_TP_CONTENT_KOTLIN)
            CONTRACT_TP_NO_MODEL_NAME_JAVA -> createContractTemplate(templateName, "java", TemplateCons.CONTRACT_TP_CONTENT_NO_MODEL_JAVA)
            CONTRACT_TP_NO_MODEL_NAME_KOTLIN -> createContractTemplate(templateName, "kt", TemplateCons.CONTRACT_TP_CONTENT_NO_MODEL_KOTLIN)
            VIEW_IMPL_TP_ACTIVITY_JAVA -> createContractTemplate(templateName, "java", TemplateCons.COMMON_IMPL_TP_CONTENT_JAVA)
            VIEW_IMPL_TP_FRAGMENT_JAVA -> createContractTemplate(templateName, "java", TemplateCons.COMMON_IMPL_TP_CONTENT_JAVA)
            PRESENTER_IMPL_TP_JAVA -> createContractTemplate(templateName, "java", TemplateCons.COMMON_IMPL_TP_CONTENT_JAVA)
            MODEL_IMPL_TP_JAVA -> createContractTemplate(templateName, "java", TemplateCons.COMMON_IMPL_TP_CONTENT_JAVA)
            VIEW_IMPL_TP_ACTIVITY_KOTLIN -> createContractTemplate(templateName, "kt", TemplateCons.COMMON_IMPL_TP_CONTENT_KOTLIN)
            VIEW_IMPL_TP_FRAGMENT_KOTLIN -> createContractTemplate(templateName, "kt", TemplateCons.COMMON_IMPL_TP_CONTENT_KOTLIN)
            PRESENTER_IMPL_TP_KOTLIN -> createContractTemplate(templateName, "kt", TemplateCons.COMMON_IMPL_TP_CONTENT_KOTLIN)
            MODEL_IMPL_TP_KOTLIN -> createContractTemplate(templateName, "kt", TemplateCons.COMMON_IMPL_TP_CONTENT_KOTLIN)
        }

        return if (cacheTemplate.containsKey(templateName)) cacheTemplate[templateName] as FileTemplate else null
    }


}


