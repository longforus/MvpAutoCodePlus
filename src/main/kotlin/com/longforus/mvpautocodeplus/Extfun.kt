package com.longforus.mvpautocodeplus

/**
 * Created by XQ Yang on 2018/6/25  19:17.
 * Description :
 */

fun <E> MutableSet<E>.eAdd(vararg e: E): Set<E> {
    if (e.size > 1) {
        this.addAll(e)
    } else if (e.isNotEmpty()) {
        this.add(e[0])
    }
    return this
}

fun String.lastDotContent() = this.substring(this.lastIndexOf(".") + 1, this.length)