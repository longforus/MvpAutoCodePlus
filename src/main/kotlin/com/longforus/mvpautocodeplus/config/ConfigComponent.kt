package com.longforus.mvpautocodeplus.config

import com.intellij.ide.BrowserUtil
import com.intellij.ide.util.PropertiesComponent
import com.intellij.ide.util.TreeClassChooserFactory
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.longforus.mvpautocodeplus.*
import com.longforus.mvpautocodeplus.ui.ConfigForm
import java.awt.event.ItemEvent
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JTextField

/**
 * Created by XQ Yang on 2018/6/25  14:12.
 * Description : 配置界面
 */
class ConfigComponent : SearchableConfigurable {


    override fun getId(): String {
        return displayName
    }

    private val mCp: ConfigForm by lazy { ConfigForm() }
    lateinit var state: PropertiesComponent

    private val mProject: Project by lazy {
        if (ProjectManager.getInstance().openProjects.isNotEmpty()) {
            ProjectManager.getInstance().openProjects[0]
        } else {
            ProjectManager.getInstance().defaultProject
        }
    }
    private val classChooserFactory by lazy { TreeClassChooserFactory.getInstance(mProject) }

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
        state = PropertiesComponent.getInstance(mProject)
        if (state.getBoolean(USE_PROJECT_CONFIG, false)) {
            mCp.mCurrentProjectRadioButton.isSelected = true
        } else {
            state = PropertiesComponent.getInstance()
        }
        loadValues()
        mCp.mGlobalRadioButton.addItemListener {
            if (it.id != ItemEvent.ITEM_STATE_CHANGED) {
                return@addItemListener
            }
            state = if (mCp.mGlobalRadioButton.isSelected) {
                PropertiesComponent.getInstance()
            } else {
                PropertiesComponent.getInstance(mProject)
            }
            loadValues()
        }
        mCp.lk_look_detail.setListener({ _, _ ->
            BrowserUtil.browse("https://github.com/longforus/MvpAutoCodePlus/blob/master/README.md")
        }, "https://github.com/longforus")
        return mCp.mPanel
    }

    private fun loadValues() {
        mCp.tv_v_name.text = state.getValue(SUPER_VIEW)
        setClassChooser(mCp.btn_view_select, "Select Super View Interface", mCp.tv_v_name)
        mCp.tv_p_name.text = state.getValue(SUPER_PRESENTER)
        setClassChooser(mCp.btn_p_select, "Select Super Presenter Interface", mCp.tv_p_name)
        mCp.tv_m_name.text = state.getValue(SUPER_MODEL)
        setClassChooser(mCp.btn_m_select, "Select Super Model Interface", mCp.tv_m_name)
        mCp.tv_view_activity.text = state.getValue(SUPER_VIEW_ACTIVITY)
        setClassChooser(mCp.btn_view_a_select, "Select View extends super Activity", mCp.tv_view_activity, true)
        mCp.tv_view_fragment.text = state.getValue(SUPER_VIEW_FRAGMENT)
        setClassChooser(mCp.btn_view_f_select, "Select View extends super Fragment", mCp.tv_view_fragment, true)
        mCp.tv_presenter_impl.text = state.getValue(SUPER_PRESENTER_IMPL)
        setClassChooser(mCp.btn_pi_select, "Select Presenter extends super Class", mCp.tv_presenter_impl, true)
        mCp.tv_model_impl.text = state.getValue(SUPER_MODEL_IMPL)
        setClassChooser(mCp.btn_mi_select, "Select Model extends super Class", mCp.tv_model_impl, true)
        mCp.et_comment_author.text = state.getValue(COMMENT_AUTHOR)
    }

    private fun setClassChooser(jButton: JButton?, title: String, tv: JTextField?, append: Boolean = false) {
        if (jButton?.actionListeners?.isNotEmpty() == true) {
            return
        }
        jButton?.addActionListener {
            val projectScopeChooser = classChooserFactory.createProjectScopeChooser(title)
            projectScopeChooser.showDialog()
            if (projectScopeChooser.selected != null) {
                if (append && !tv?.text.isNullOrEmpty()) {
                    tv?.text = "${tv?.text};${projectScopeChooser.selected.qualifiedName}"
                } else {
                    tv?.text = projectScopeChooser.selected.qualifiedName
                }
            }
        }
    }

}