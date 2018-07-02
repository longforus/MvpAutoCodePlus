package com.longforus.mvpautocodeplus.maker

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.ide.fileTemplates.impl.FileTemplateManagerImpl
import com.intellij.openapi.project.Project
import com.longforus.mvpautocodeplus.CONTRACT_TP_NAME_JAVA
import com.longforus.mvpautocodeplus.CONTRACT_TP_NAME_KOTLIN
import com.longforus.mvpautocodeplus.TemplateCons
import com.longforus.mvpautocodeplus.VIEW_IMPL_TP_ACTIVITY_JAVA

/**
 * Created by XQ Yang on 2018/6/28  10:46.
 * Description :
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
            CONTRACT_TP_NAME_JAVA -> createContractTemplate(CONTRACT_TP_NAME_JAVA, "java", TemplateCons.CONTRACT_TP_CONTENT_JAVA)
            CONTRACT_TP_NAME_KOTLIN -> createContractTemplate(CONTRACT_TP_NAME_KOTLIN, "kt", TemplateCons.CONTRACT_TP_CONTENT_KOTLIN)
            VIEW_IMPL_TP_ACTIVITY_JAVA -> createContractTemplate(VIEW_IMPL_TP_ACTIVITY_JAVA, "java", TemplateCons.VIEW_IMPL_TP_CONTENT_ACTIVITY_JAVA)
        }

        return if (cacheTemplate.containsKey(templateName)) cacheTemplate[templateName] as FileTemplate else null
    }


}


