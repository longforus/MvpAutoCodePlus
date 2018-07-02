package com.longforus.mvpautocodeplus.maker

import com.intellij.codeInsight.generation.GenerateMembersUtil
import com.intellij.codeInsight.generation.OverrideImplementExploreUtil
import com.intellij.codeInsight.generation.OverrideImplementUtil.*
import com.intellij.codeInsight.generation.PsiGenerationInfo
import com.intellij.codeInsight.generation.PsiMethodMember
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.Result
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.util.IncorrectOperationException
import com.intellij.util.containers.ContainerUtil

/**
 * Created by XQ Yang on 2018/7/2  15:53.
 * Description : 实现抽象方法
 */

fun chooseAndOverrideOrImplementMethods(project: Project,
    editor: Editor,
    aClass: PsiClass,
    toImplement: Boolean) {
    ApplicationManager.getApplication().assertReadAccessAllowed()
    val candidates = OverrideImplementExploreUtil.getMethodsToOverrideImplement(aClass, toImplement)
    val secondary = if (toImplement || aClass.isInterface)
        ContainerUtil.newArrayList()
    else
        OverrideImplementExploreUtil.getMethodsToOverrideImplement(aClass, true)

    if (candidates.isEmpty() && secondary.isEmpty()) return

    if (toImplement) {
        val iterator = candidates.iterator()
        while (iterator.hasNext()) {
            val candidate = iterator.next()
            val element = candidate.element
            if (element is PsiMethod && element.hasModifierProperty(PsiModifier.DEFAULT)) {
                iterator.remove()
                secondary.add(candidate)
            }
        }
    }


    val onlyPrimary = ContainerUtil.map2Array(candidates, PsiMethodMember::class.java) { s -> PsiMethodMember(s) }
    val allList = ArrayList<PsiMethodMember>()
    for (member in onlyPrimary) {
        allList.add(member)
    }
    object : WriteCommandAction<Any?>(project, aClass.containingFile) {
        @Throws()
        override fun run(result: Result<Any?>) {
            overrideOrImplementMethodsInRightPlace(editor, aClass, allList, false, true)
        }
    }.executeSilently()
}


fun overrideOrImplementMethodsInRightPlace(editor: Editor,
    aClass: PsiClass,
    candidates: Collection<PsiMethodMember>,
    copyJavadoc: Boolean,
    insertOverrideWherePossible: Boolean) {
    try {
        val offset = 0
        var brace = aClass.lBrace
        if (brace == null) {
            val psiClass = JavaPsiFacade.getInstance(aClass.project).elementFactory.createClass("X")
            brace = aClass.addRangeAfter(psiClass.lBrace, psiClass.rBrace, aClass.lastChild)
        }

        val lbraceOffset = brace!!.textOffset
        val resultMembers: MutableList<PsiGenerationInfo<PsiMethod>>
        if (offset <= lbraceOffset || aClass.isEnum) {
            resultMembers = java.util.ArrayList()
            for (candidate in candidates) {
                val prototypes = overrideOrImplementMethod(aClass, candidate.element, candidate.substitutor, copyJavadoc, insertOverrideWherePossible)
                val infos = convert2GenerationInfos(prototypes)
                for (info in infos) {
                    val anchor = getDefaultAnchorToOverrideOrImplement(aClass, candidate.element, candidate.substitutor)
                    info.insert(aClass, anchor, true)
                    resultMembers.add(info)
                }
            }
        } else {
            val prototypes = overrideOrImplementMethods(aClass, candidates, copyJavadoc, insertOverrideWherePossible)
            resultMembers = GenerateMembersUtil.insertMembersAtOffset(aClass, offset, prototypes)
        }

        if (!resultMembers.isEmpty()) {
            resultMembers[0].positionCaret(editor, true)
        }
    } catch (e: IncorrectOperationException) {
        e.printStackTrace()
    }

}