package istanbul.codify.monju.helper;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class Cache<T> {

    private WeakHashMap<String, WeakReference<T>> mMap;

    public Cache() {
        mMap = new WeakHashMap<>();
    }

    public void put(String key, T object) {
        if (!mMap.containsKey(key)) {
            WeakReference<T> reference = new WeakReference<>(object);
            mMap.put(key, reference);
        }
    }

    public boolean contains(String key) {
        return mMap.containsKey(key);
    }

    public @Nullable
    final T get(String key) {
        if (!mMap.containsKey(key)) {
            WeakReference<T> reference = mMap.get(key);
            return reference.get();
        } else {
            throw new IllegalStateException("Does not have given key");
        }
    }

    @SuppressWarnings({"UnusedAssignment", "unused"})
    public void remove(String key) {
        WeakReference<T> reference = mMap.remove(key);
        reference = null;
    }
}