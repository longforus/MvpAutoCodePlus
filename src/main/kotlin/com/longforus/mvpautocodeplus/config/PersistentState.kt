package com.longforus.mvpautocodeplus.config

import com.intellij.configurationStore.APP_CONFIG
import com.intellij.ide.util.PropertiesComponent
import com.intellij.ide.util.PropertiesComponentImpl
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.containers.ContainerUtil
import org.jdom.Element
import org.jdom.Verifier
import org.jetbrains.annotations.NonNls
import java.util.*

/**
 * Created by XQ Yang on 2018/6/26  10:46.
 * Description :
 */
@State(name = "mvpAutoCodePlus", storages = [(Storage("$APP_CONFIG$/mvpAutoCodePlus.xml"))])
class PersistentState : PropertiesComponent(), PersistentStateComponent<Element> {


    private val LOG = Logger.getInstance(PropertiesComponentImpl::class.java)

    private val myMap = ContainerUtil.newConcurrentMap<String, String>()

    @NonNls
    private val ELEMENT_PROPERTY = "property"
    @NonNls
    private val ATTRIBUTE_NAME = "name"
    @NonNls
    private val ATTRIBUTE_VALUE = "value"

    fun getComponentName(): String {
        return "PropertiesComponent"
    }


    private fun doPut(key: String, value: String) {
        val reason = Verifier.checkCharacterData(key)
        if (reason != null) {
            LOG.error(reason)
        }
        myMap[key] = value
        incModificationCount()
    }


    override fun getState(): Element? {
        val parentNode = Element("state")
        val keys = ArrayList(myMap.keys)
        keys.sort()
        for (key in keys) {
            val value = myMap[key]
            if (value != null) {
                val element = Element(ELEMENT_PROPERTY)
                element.setAttribute(ATTRIBUTE_NAME, key)
                element.setAttribute(ATTRIBUTE_VALUE, value)
                parentNode.addContent(element)
            }
        }
        return parentNode
    }

    override fun loadState(parentNode: Element) {
        myMap.clear()
        for (e in parentNode.getChildren(ELEMENT_PROPERTY)) {
            val name = e.getAttributeValue(ATTRIBUTE_NAME)
            if (name != null) {
                myMap[name] = e.getAttributeValue(ATTRIBUTE_VALUE)
            }
        }
    }

    override fun getValue(name: String): String? {
        return myMap[name]
    }

    override fun setValue(name: String, value: String?) {
        if (value == null) {
            unsetValue(name)
        } else {
            doPut(name, value)
        }
    }

    override fun setValue(name: String, value: String?, defaultValue: String?) {
        if (value == null || value == defaultValue) {
            unsetValue(name)
        } else {
            doPut(name, value)
        }
    }

    override fun setValue(name: String, value: Float, defaultValue: Float) {
        if (value == defaultValue) {
            unsetValue(name)
        } else {
            doPut(name, value.toString())
        }
    }

    override fun setValue(name: String, value: Int, defaultValue: Int) {
        if (value == defaultValue) {
            unsetValue(name)
        } else {
            doPut(name, value.toString())
        }
    }

    override fun setValue(name: String, value: Boolean, defaultValue: Boolean) {
        if (value == defaultValue) {
            unsetValue(name)
        } else {
            setValue(name, value.toString())
        }
    }

    override fun unsetValue(name: String) {
        myMap.remove(name)
        incModificationCount()
    }

    override fun isValueSet(name: String): Boolean {
        return myMap.containsKey(name)
    }

    override fun getValues(@NonNls name: String): Array<String>? {
        val value = getValue(name)
        return value?.split("\n".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
    }

    override fun setValues(@NonNls name: String, values: Array<String>?) {
        if (values == null) {
            setValue(name, null)
        } else {
            setValue(name, StringUtil.join(values, "\n"))
        }
    }

}