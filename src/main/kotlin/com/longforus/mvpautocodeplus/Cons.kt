package com.longforus.mvpautocodeplus

/**
 * Created by XQ Yang on 2018/6/26  11:14.
 * Description : 常量
 */

fun getContractName(name: String) = "I${name}Contract"

fun getViewInfName(name: String) = "I${name}View"
fun getPresenterInfName(name: String) = "I${name}Presenter"
fun getModelInfName(name: String) = "I${name}Model"

const val USE_PROJECT_CONFIG = "use_project_config"
const val GENERATE_MODEL_CONFIG = "generate_model_config"
const val GOTO_SETTING = "Please go to File-> Settings -> OtherSettings -> MvpAutoCodePlus set."
const val IS_NOT_SET = "Is not set"
const val NO_SUPER_CLASS = "The implementation class will have no superclass"

const val CONTRACT = "contract"
const val VIEW = "view"
const val PRESENTER = "presenter"
const val MODEL = "model"


const val SUPER_VIEW = "super_view"
const val SUPER_PRESENTER = "super_presenter"
const val SUPER_MODEL = "super_model"
const val SUPER_VIEW_ACTIVITY = "super_view_activity"
const val SUPER_VIEW_FRAGMENT = "super_view_fragment"
const val SUPER_PRESENTER_IMPL = "super_presenter_impl"
const val SUPER_MODEL_IMPL = "super_model_impl"
const val COMMENT_AUTHOR = "comment_author"


const val CONTRACT_TP_NAME_JAVA = "JavaMvpAutoCodePlusContract"
const val CONTRACT_TP_NAME_KOTLIN = "KotlinMvpAutoCodePlusContract"
const val CONTRACT_TP_NO_MODEL_NAME_JAVA = "JavaMvpAutoCodePlusContractNoModel"
const val CONTRACT_TP_NO_MODEL_NAME_KOTLIN = "KotlinMvpAutoCodePlusContractNoModel"
const val VIEW_IMPL_TP_ACTIVITY_JAVA = "JavaMvpAutoCodePlusViewActivityImpl"
const val VIEW_IMPL_TP_ACTIVITY_KOTLIN = "KotlinMvpAutoCodePlusViewActivityImpl"
const val VIEW_IMPL_TP_FRAGMENT_JAVA = "JavaMvpAutoCodePlusViewFragmentImpl"
const val VIEW_IMPL_TP_FRAGMENT_KOTLIN = "KotlinMvpAutoCodePlusViewFragmentImpl"
const val PRESENTER_IMPL_TP_JAVA = "JavaMvpAutoCodePlusPresenterImpl"
const val PRESENTER_IMPL_TP_KOTLIN = "KotlinMvpAutoCodePlusPresenterImpl"
const val MODEL_IMPL_TP_JAVA = "JavaMvpAutoCodePlusModelImpl"
const val MODEL_IMPL_TP_KOTLIN = "KotlinMvpAutoCodePlusModelImpl"