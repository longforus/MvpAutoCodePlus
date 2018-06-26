package com.longforus.mvpautocodeplus.maker

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile

/**
 * Created by XQ Yang on 2018/6/26  14:06.
 * Description :
 */
interface IMakeContract {
    val name: String
    val dir: PsiDirectory
    val isJava: Boolean
    fun checkExits()
    fun makeContract(): PsiFile
}