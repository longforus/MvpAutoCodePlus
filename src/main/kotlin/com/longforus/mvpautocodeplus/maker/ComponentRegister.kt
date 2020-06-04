package com.longforus.mvpautocodeplus.maker

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.ReadonlyStatusHandler
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiPackage
import org.jetbrains.android.dom.manifest.Application
import org.jetbrains.android.dom.manifest.ApplicationComponent
import org.jetbrains.android.dom.manifest.Manifest
import org.jetbrains.android.dom.resources.ResourceValue
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.facet.AndroidRootUtil
import org.jetbrains.android.util.AndroidUtils

/**
 * @describe
 * @author  longforus
 * @date 2020/3/18  15:11
 */
object ComponentRegister {

    fun registerActivity(project:Project,aClass: PsiClass?, aPackage: PsiPackage?, facet: AndroidFacet, label: String?) {
        val manifestFile = AndroidRootUtil.getPrimaryManifestFile(facet)
        if (manifestFile != null && ReadonlyStatusHandler.ensureFilesWritable(facet.module.project, manifestFile)) {
            val manifest = AndroidUtils.loadDomElement(facet.module, manifestFile,
                Manifest::class.java)
            if (manifest != null) {
                val packageName = manifest.getPackage().value
                if (packageName == null || packageName.isEmpty()) {
                    manifest.getPackage().value = aPackage?.qualifiedName
                }
                val application = manifest.application
                if (application != null) {
                    WriteCommandAction.writeCommandAction(project, aClass!!.containingFile).run<Throwable> {
                        val component = addToManifest( aClass, application)
                        if (component != null && !label.isNullOrEmpty()) {
                            component.label.value = ResourceValue.literal(label)
                        }
                    }

                }
            }
        }
    }


    fun addToManifest(aClass: PsiClass, application: Application): ApplicationComponent? {
        val activity = application.addActivity()
        activity.activityClass.value = aClass
       return activity
    }

}