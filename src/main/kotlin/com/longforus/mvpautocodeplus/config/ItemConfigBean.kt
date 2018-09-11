package com.longforus.mvpautocodeplus.config

import com.intellij.ide.util.PropertiesComponent

/**
 * Created by XQ Yang on 2018/6/28  16:20.
 * Description :
 */
data class ItemConfigBean(val name: String, val isJava: Boolean = true, val isActivity: Boolean = true, val vImpl: String, val pImpl: String, val mImpl: String,
    val state: PropertiesComponent, val generateModel: Boolean = true)