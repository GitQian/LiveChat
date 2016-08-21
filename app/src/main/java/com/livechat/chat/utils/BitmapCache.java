package com.livechat.chat.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;


public class BitmapCache implements ImageLoader.ImageCache{
	// 如果想让整个项目共用一个图片缓存,那么这里可以将mCache设置成静态
	private static LruCache<String, Bitmap> mCache;

	public BitmapCache() {
		if (mCache == null) {
			int maxSize = 16 * 1024 * 1024;
			mCache = new LruCache<String, Bitmap>(maxSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		}
	}

	public Bitmap getBitmap(String url) {
		return mCache.get(url);
	}

	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
		Log.d(getClass().getSimpleName(), "cacheSize/maxSize:" + mCache.size() + "/" + mCache.maxSize());
	}
}