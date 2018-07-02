package com.longforus.mvpautocodeplus.maker

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.longforus.mvpautocodeplus.*
import com.longforus.mvpautocodeplus.config.PersistentState

/**
 * Created by XQ Yang on 2018/6/28  14:18.
 * Description :
 */

object TemplateParamFactory {
    private val state: PersistentState = ServiceManager.getService(PersistentState::class.java)


    fun getParam4TemplateName(templateName: String, name: String, superImplName: String, contract: PsiFile?): Map<String, String?> {
        val liveTemplateParam = HashMap<String, String?>()
        when (templateName) {
            CONTRACT_TP_NAME_JAVA, CONTRACT_TP_NAME_KOTLIN -> {
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
            VIEW_IMPL_TP_ACTIVITY_JAVA -> {
                val (noGSuperName, superMGenericValue) = getNameAndGenericType(SUPER_VIEW_ACTIVITY, false, name, superImplName)
                val javaFile = contract as PsiJavaFile
                liveTemplateParam["CONTRACT"] = "${javaFile.packageName}.${getContractName(name)}"
                liveTemplateParam["A_IMPL"] = noGSuperName
                liveTemplateParam["VG"] = superMGenericValue
            }
//                <I${NAME}Contract.View,I${NAME}Contract.Presenter>
        }
        return liveTemplateParam
    }


    fun getNameAndGenericType(type: String, isContract: Boolean = true, enterName: String = "", selectedValue: String = ""): Pair<String?, String> {
        val setValue = if (selectedValue.isNotEmpty()) selectedValue else state.getValue(type)
        if (setValue.isNullOrEmpty()) {
            Messages.showErrorDialog("Super Interface name is null !", "Error")
            throw IllegalArgumentException("Super Interface name is null !")
        }
        val indexOf = setValue?.indexOf("<") ?: -1
        var generic = ""
        var resultName = setValue
        if (indexOf > -1) {
            resultName = setValue?.substring(0, indexOf)
            var g = setValue?.substring(indexOf, setValue.length)
            g = g?.replace("V", if (isContract) "View" else "${getContractName(enterName)}.View")
            g = g?.replace("P", if (isContract) "Presenter" else "${getContractName(enterName)}.Presenter")
            g = g?.replace("M", if (isContract) "Model" else "${getContractName(enterName)}.Model")
            generic = g ?: ""
        }
        return resultName to generic
    }

}

