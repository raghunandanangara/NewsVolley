package com.raghu.newsvolley;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/** Volley Library In-Memory(RAM) Cache used exclusively for Images 
 *  @Note Disk Cache(File) can also be implemented if you need more cache.
 *  Please check Volley Documentation */
public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache{

	public BitmapLruCache() {
        this(getDefaultLruCacheSize());
    }
 
    public BitmapLruCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }
 
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }
 
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }
 
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
 
    public static int getDefaultLruCacheSize() {
        final int maxMemory =
                (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
 
        return cacheSize;
    }
}
