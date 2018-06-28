package com.longforus.mvpautocodeplus.config

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.options.SearchableConfigurable
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

    override fun isModified(): Boolean {
        return mCp.tv_v_name.text != state.getValue(SUPER_VIEW) ||
            mCp.tv_p_name.text != state.getValue(SUPER_PRESENTER) ||
            mCp.tv_m_name.text != state.getValue(SUPER_MODEL) ||
            mCp.tv_model_impl.text != state.getValue(SUPER_MODEL_IMPL) ||
            mCp.tv_presenter_impl.text != state.getValue(SUPER_PRESENTER_IMPL) ||
            mCp.tv_view_fragment.text != state.getValue(SUPER_VIEW_FRAGMENT) ||
            mCp.tv_view_activity.text != state.getValue(SUPER_VIEW_ACTIVITY)
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
    }

    override fun createComponent(): JComponent? {
        mCp.tv_v_name.text = state.getValue(SUPER_VIEW)
        mCp.tv_p_name.text = state.getValue(SUPER_PRESENTER)
        mCp.tv_m_name.text = state.getValue(SUPER_MODEL)
        mCp.tv_view_activity.text = state.getValue(SUPER_VIEW_ACTIVITY)
        mCp.tv_view_fragment.text = state.getValue(SUPER_VIEW_FRAGMENT)
        mCp.tv_presenter_impl.text = state.getValue(SUPER_PRESENTER_IMPL)
        mCp.tv_model_impl.text = state.getValue(SUPER_MODEL_IMPL)
        return mCp.mPanel
    }

}