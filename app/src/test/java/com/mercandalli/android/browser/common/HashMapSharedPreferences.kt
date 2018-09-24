package com.mercandalli.android.browser.common

import android.content.SharedPreferences

import java.util.HashMap

/**
 * A [SharedPreferences] that uses an internal [Map]
 * to store the items.
 */
class HashMapSharedPreferences : SharedPreferences {

    private val map: MutableMap<String, Any>

    init {
        map = HashMap()
    }

    override fun getAll(): Map<String, *> {
        return map
    }

    override fun getString(key: String, defValue: String?): String? {
        return get(map, key, defValue)
    }

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? {
        return get(map, key, defValues)
    }

    override fun getInt(key: String, defValue: Int): Int {
        return get(map, key, defValue)!!
    }

    override fun getLong(key: String, defValue: Long): Long {
        return get(map, key, defValue)!!
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return get(map, key, defValue)!!
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return get(map, key, defValue)!!
    }

    override fun contains(key: String): Boolean {
        return map.containsKey(key)
    }

    override fun edit(): SharedPreferences.Editor {
        return MapEditor(map)
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {

    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {

    }

    private class MapEditor constructor(
            private val map: MutableMap<String, Any>
    ) : SharedPreferences.Editor {

        override fun putString(key: String, value: String?): SharedPreferences.Editor {
            map[key] = value!!
            return this
        }

        override fun putStringSet(key: String, values: Set<String>?): SharedPreferences.Editor {
            map[key] = values!!
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            map[key] = value
            return this
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor {
            map[key] = value
            return this
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
            map[key] = value
            return this
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
            map[key] = value
            return this
        }

        override fun remove(key: String): SharedPreferences.Editor {
            map.remove(key)
            return this
        }

        override fun clear(): SharedPreferences.Editor {
            map.clear()
            return this
        }

        override fun commit(): Boolean {
            return true
        }

        override fun apply() {

        }
    }

    @Suppress("UNCHECKED_CAST")
    private operator fun <T> get(map: Map<String, *>, key: String, defValue: T?): T? {
        return if (map.containsKey(key)) {
            map[key] as T?
        } else {
            defValue
        }
    }
}
