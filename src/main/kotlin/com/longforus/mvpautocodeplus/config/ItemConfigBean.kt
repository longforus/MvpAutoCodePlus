package com.longforus.mvpautocodeplus.config

/**
 * Created by XQ Yang on 2018/6/28  16:20.
 * Description :
 */
data class ItemConfigBean(val name: String, val isJava: Boolean = true, val isActivity: Boolean = true, val implItems: Triple<Boolean, Boolean, Boolean> = Triple(true, true, true))