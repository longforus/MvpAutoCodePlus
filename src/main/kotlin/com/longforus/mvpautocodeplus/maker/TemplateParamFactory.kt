package com.longforus.mvpautocodeplus.maker

import com.intellij.openapi.components.ServiceManager
import com.longforus.mvpautocodeplus.CONTRACT_TP_NAME_JAVA
import com.longforus.mvpautocodeplus.SUPER_MODEL
import com.longforus.mvpautocodeplus.SUPER_PRESENTER
import com.longforus.mvpautocodeplus.SUPER_VIEW
import com.longforus.mvpautocodeplus.config.PersistentState

/**
 * Created by XQ Yang on 2018/6/28  14:18.
 * Description :
 */

object TemplateParamFactory {
    private val state: PersistentState = ServiceManager.getService(PersistentState::class.java)


    fun getParam4TemplateName(templateName: String): Map<String, String?> {
        val liveTemplateParam = HashMap<String, String?>()
        when (templateName) {
            CONTRACT_TP_NAME_JAVA -> {
                val (superVNameNoGeneric, superVGenericValue) = getNameAndGenericType(SUPER_VIEW)
                val (superPNameNoGeneric, superPGenericValue) = getNameAndGenericType(SUPER_PRESENTER)
                val (superMNameNoGeneric, superMGenericValue) = getNameAndGenericType(SUPER_MODEL)
                liveTemplateParam["V"] = superVNameNoGeneric
                liveTemplateParam["M"] = superMNameNoGeneric
                liveTemplateParam["P"] = superPNameNoGeneric
                liveTemplateParam["VG"] = superVGenericValue
                liveTemplateParam["PG"] = superPGenericValue
                liveTemplateParam["MG"] = superMGenericValue
            }
        }
        return liveTemplateParam
    }


    fun getNameAndGenericType(type: String): Pair<String?, String> {
        val name = state.getValue(type)
        if (name.isNullOrEmpty()) {
            throw IllegalArgumentException("Super Interface name is null !")
        }
        val indexOf = name?.indexOf("<") ?: -1
        var generic = ""
        var resultName = name
        if (indexOf > -1) {
            resultName = name?.substring(0, indexOf)
            var g = name?.substring(indexOf, name.length)
            g = g?.replace("V", "View")
            g = g?.replace("P", "Presenter")
            g = g?.replace("M", "Model")
            generic = g ?: ""
        }
        return resultName to generic
    }

}

