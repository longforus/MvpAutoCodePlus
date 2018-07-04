package com.longforus.mvpautocodeplus.config

import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.Messages
import com.longforus.mvpautocodeplus.*
import com.longforus.mvpautocodeplus.ui.ConfigForm
import javax.swing.JComponent

/**
 * Created by XQ Yang on 2018/6/25  14:12.
 * Description : 配置界面
 */
class ConfigComponent : SearchableConfigurable {


    override fun getId(): String {
        return displayName
    }

    private val mCp: ConfigForm by lazy { ConfigForm() }
    private val state: PersistentState by lazy { ServiceManager.getService(PersistentState::class.java) }
    private val fcd = FileChooserDescriptor(true, false, false, false, false, false)

    private val project: Project by lazy { ProjectManager.getInstance().openProjects[0] }
    override fun isModified(): Boolean {
        return mCp.tv_v_name.text != state.getValue(SUPER_VIEW) ||
            mCp.tv_p_name.text != state.getValue(SUPER_PRESENTER) ||
            mCp.tv_m_name.text != state.getValue(SUPER_MODEL) ||
            mCp.tv_model_impl.text != state.getValue(SUPER_MODEL_IMPL) ||
            mCp.tv_presenter_impl.text != state.getValue(SUPER_PRESENTER_IMPL) ||
            mCp.tv_view_fragment.text != state.getValue(SUPER_VIEW_FRAGMENT) ||
            mCp.tv_view_activity.text != state.getValue(SUPER_VIEW_ACTIVITY) ||
            mCp.et_comment_author.text != state.getValue(COMMENT_AUTHOR)
    }

    override fun getDisplayName(): String {
        return "MvpAutoCodePlus"
    }

    override fun apply() {
        state.setValue(SUPER_VIEW, mCp.tv_v_name.text)
        state.setValue(SUPER_PRESENTER, mCp.tv_p_name.text)
        state.setValue(SUPER_MODEL, mCp.tv_m_name.text)
        state.setValue(SUPER_VIEW_ACTIVITY, mCp.tv_view_activity.text)
        state.setValue(SUPER_VIEW_FRAGMENT, mCp.tv_view_fragment.text)
        state.setValue(SUPER_PRESENTER_IMPL, mCp.tv_presenter_impl.text)
        state.setValue(SUPER_MODEL_IMPL, mCp.tv_model_impl.text)
        state.setValue(COMMENT_AUTHOR, mCp.et_comment_author.text)
    }

    override fun createComponent(): JComponent? {
        val value = state.getValue(SUPER_VIEW)
        mCp.tv_v_name.text = value
        // TODO: 2018/7/2  实现class选择
//        val dialogImpl = FileChooserDialogImpl(fcd, project)
//        val split = value?.split(";")
//        project.projectFile.findChild()
        mCp.btn_view_select.addActionListener {
//            val choose = dialogImpl.choose(project, null)
            Messages.showMessageDialog("还没有找到合适的方法来实现class的选择", "待开发", AllIcons.General.ErrorDialog)
        }
        mCp.tv_p_name.text = state.getValue(SUPER_PRESENTER)
        mCp.tv_m_name.text = state.getValue(SUPER_MODEL)
        mCp.tv_view_activity.text = state.getValue(SUPER_VIEW_ACTIVITY)
        mCp.tv_view_fragment.text = state.getValue(SUPER_VIEW_FRAGMENT)
        mCp.tv_presenter_impl.text = state.getValue(SUPER_PRESENTER_IMPL)
        mCp.tv_model_impl.text = state.getValue(SUPER_MODEL_IMPL)
        mCp.et_comment_author.text = state.getValue(COMMENT_AUTHOR)
        mCp.lk_look_detail.setListener({ aSource, aLinkData ->
            BrowserUtil.browse("https://github.com/longforus/MvpAutoCodePlus/blob/master/README.md")
        }, "https://github.com/longforus")
        return mCp.mPanel
    }

}