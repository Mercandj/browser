package com.mercandalli.android.browser.common;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;

/**
 * A {@link SharedPreferences} that uses an internal {@link Map}
 * to store the items.
 */
public class HashMapSharedPreferences implements SharedPreferences {

    private final Map<String, Object> map;

    public HashMapSharedPreferences() {
        map = new HashMap<>();
    }

    public HashMapSharedPreferences(Map<String, Object> initialMap) {
        map = new HashMap<>();
        if (initialMap != null) {
            map.putAll(initialMap);
        }
    }

    @Override
    public Map<String, ?> getAll() {
        return map;
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        return get(map, key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return get(map, key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        return get(map, key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return get(map, key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return get(map, key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return get(map, key, defValue);
    }

    @Override
    public boolean contains(String key) {
        return map.containsKey(key);
    }

    @Override
    public MapEditor edit() {
        return new MapEditor(map);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {

    }

    private static class MapEditor implements Editor {

        private final Map<String, Object> map;

        private MapEditor(Map<String, Object> map) {
            this.map = map;
        }

        @Override
        public Editor putString(String key, String value) {
            map.put(key, value);
            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {
            map.put(key, values);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            map.put(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            map.put(key, value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            map.put(key, value);
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            map.put(key, value);
            return this;
        }

        @Override
        public Editor remove(String key) {
            map.remove(key);
            return this;
        }

        @Override
        public Editor clear() {
            map.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return true;
        }

        @Override
        public void apply() {

        }
    }

    private static <T> T get(Map<String, ?> map, String key, T defValue) {
        if (map.containsKey(key)) {
            return (T) map.get(key);
        } else {
            return defValue;
        }
    }
}
