package com.codify.howdy.helper;

import android.support.annotation.Nullable;

import java.util.HashMap;

public abstract class Pool<T> {

    private HashMap<Class, T> mMap;

    public Pool() {
        mMap = new HashMap<>();
    }

    public @Nullable
    final T get(Class<? extends T> key) {
        try {
            if (mMap.containsKey(key)) {
                return mMap.get(key);
            }

            T supply = supply(key);
            mMap.put(key, supply);
            return supply;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected abstract T supply(Class<? extends T> key) throws Exception;
}