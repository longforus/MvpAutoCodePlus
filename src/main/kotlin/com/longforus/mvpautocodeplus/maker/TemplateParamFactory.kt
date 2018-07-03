package com.longforus.mvpautocodeplus.maker

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.longforus.mvpautocodeplus.*
import com.longforus.mvpautocodeplus.config.PersistentState
import java.util.*

/**
 * Created by XQ Yang on 2018/6/28  14:18.
 * Description :
 */

object TemplateParamFactory {
    private val state: PersistentState = ServiceManager.getService(PersistentState::class.java)


    fun getParam4TemplateName(templateName: String, enterName: String, superImplName: String, contract: PsiFile?): Map<String, String?> {
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
            VIEW_IMPL_TP_ACTIVITY_JAVA, VIEW_IMPL_TP_ACTIVITY_KOTLIN -> {
                setCommonParam(enterName, superImplName, contract, liveTemplateParam, templateName)
                liveTemplateParam["IMPL_TYPE"] = "Activity"
                liveTemplateParam["TYPE"] = "View"
            }
            VIEW_IMPL_TP_FRAGMENT_JAVA, VIEW_IMPL_TP_FRAGMENT_KOTLIN -> {
                setCommonParam(enterName, superImplName, contract, liveTemplateParam, templateName)
                liveTemplateParam["IMPL_TYPE"] = "Fragment"
                liveTemplateParam["TYPE"] = "View"
            }
            PRESENTER_IMPL_TP_JAVA, PRESENTER_IMPL_TP_KOTLIN -> {
                setCommonParam(enterName, superImplName, contract, liveTemplateParam, templateName)
                liveTemplateParam["IMPL_TYPE"] = "Presenter"
                liveTemplateParam["TYPE"] = "Presenter"
            }
            MODEL_IMPL_TP_JAVA, MODEL_IMPL_TP_KOTLIN -> {
                setCommonParam(enterName, superImplName, contract, liveTemplateParam, templateName)
                liveTemplateParam["IMPL_TYPE"] = "Model"
                liveTemplateParam["TYPE"] = "Model"
            }
//                <I${NAME}Contract.View,I${NAME}Contract.Presenter>
        }
        return liveTemplateParam
    }


    private fun setCommonParam(name: String, superImplName: String, contract: PsiFile?,
        liveTemplateParam: HashMap<String, String?>,
        templateName: String) {
        val (noGSuperName, superMGenericValue) = getNameAndGenericType("", false, name, superImplName)
        var packageName = ""
        if (contract is PsiJavaFile) {
            packageName = contract.packageName
        } else if (contract is org.jetbrains.kotlin.psi.KtFile) {
            packageName = contract.packageFqName.asString()
        }

        liveTemplateParam["CONTRACT"] = "$packageName.${getContractName(name)}"
        liveTemplateParam["IMPL"] = noGSuperName
        liveTemplateParam["VG"] = superMGenericValue

        if (templateName.startsWith("Kotlin")) {
            liveTemplateParam["IMPL_NP"] = liveTemplateParam["IMPL"]?.lastDotContent()
            liveTemplateParam["CONTRACT_NP"] = liveTemplateParam["CONTRACT"]?.lastDotContent()
        }
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
            val g = setValue?.substring(indexOf, setValue.length)
            val sb = StringBuilder()
            g?.forEach {
                when (it) {
                    "V"[0] -> sb.append(if (isContract) "View" else "${getContractName(enterName)}.View")
                    "P"[0] -> sb.append(if (isContract) "Presenter" else "${getContractName(enterName)}.Presenter")
                    "M"[0] -> sb.append(if (isContract) "Model" else "${getContractName(enterName)}.Model")
                    else -> sb.append(it)
                }
            }
            generic = sb.toString()
        }
        return resultName to generic
    }

}

