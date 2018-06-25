package com.longforus.mvpautocodeplus.config

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.SearchableConfigurable
import com.longforus.mvpautocodeplus.ui.ConfigForm
import javax.swing.JComponent

/**
 * Created by XQ Yang on 2018/6/25  14:12.
 * Description :
 */
class ConfigComponent : SearchableConfigurable, Disposable {
    override fun dispose() {
        mCp.mPanel = null
    }

    override fun getId(): String {
        return displayName
    }

    private val mCp: ConfigForm by lazy { ConfigForm() }


    override fun isModified(): Boolean {
        return false
    }

    override fun getDisplayName(): String {
        return "mvp Make Config"
    }

    override fun apply() {

    }

    override fun createComponent(): JComponent? {
        return mCp.mPanel
    }

}