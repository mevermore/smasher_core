package com.smasher.core.bitmap;

import android.graphics.Bitmap;

import java.util.Collection;

/**
 * @author moyu
 * @date 2017/7/26
 */

public interface MemoryCache {

    boolean put(String key, Bitmap value);

    Bitmap get(String key);

    Bitmap remove(String key);

    Collection<String> keys();

    void clear();
}
