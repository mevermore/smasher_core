package com.smasher.core.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.smasher.core.io.FileUtil;

import java.io.InputStream;

/**
 * @author moyu
 * @date 2017/4/8
 */

public class DeBitmapFactory {

    public static Bitmap createBitmap(String tag, Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        if (source == null || source.isRecycled()) {
            return null;
        }

        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            try {
                bitmap = Bitmap.createBitmap(source, x, y, width, height, m, filter);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    public static Bitmap createBitmap(String tag, Bitmap source, int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        if (source == null || source.isRecycled()) {
            return null;
        }

        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            try {
                bitmap = Bitmap.createBitmap(source, x, y, width, height);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    public static Bitmap createBitmap(String tag, int width, int height, Bitmap.Config config) {
        if (width <= 0 || height <= 0) {
            return null;
        }

        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            try {
                bitmap = Bitmap.createBitmap(width, height, config);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    /**
     * CreateBitmapByAsset
     *
     * @param context  context
     * @param fileName fileName
     * @param width    width
     * @param height   height
     * @return Bitmap
     */
    public static Bitmap createBitmapByAsset(Context context, final String tag, final String fileName, int width, int height) {
        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            byte[] bytes = FileUtil.loadAsset(context, fileName);
            if (bytes == null) {
                return null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = width;
            options.outHeight = height;
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    /**
     * 创建缩略图
     *
     * @param tag       tag
     * @param src       src
     * @param dstWidth  dstWidth
     * @param dstHeight dstHeight
     * @param filter    filter
     * @return
     */
    public static Bitmap createScaledBitmap(String tag, Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        if (src == null || src.isRecycled()) {
            return null;
        }
        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            try {
                bitmap = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    /**
     * 创建圆形图
     *
     * @param tag      tag
     * @param src      src
     * @param diameter diameter
     * @param filter   filter
     * @return
     */
    public static Bitmap craeteCircleBitmap(String tag, Bitmap src, int diameter, boolean filter) {
        if (src == null || src.isRecycled()) {
            return null;
        }
        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            BitmapManager.removeBitmap(tag);
            try {
                //先将原图按照圆形直径缩放
                src = Bitmap.createScaledBitmap(src, diameter, diameter, filter);

                //再画圆
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                bitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                canvas.drawCircle(diameter / 2f, diameter / 2f, diameter / 2f, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(src, 0, 0, paint);
            } catch (OutOfMemoryError error) {
                error.printStackTrace();
            }
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    public static Bitmap decodeResource(Resources res, String tag, int resId) {
        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = BitmapFactory.decodeResource(res, resId);
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    public static Bitmap decodeStream(Resources res, String tag, InputStream is, BitmapFactory.Options options) {
        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    public static Bitmap decodeBytes(String tag, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Bitmap bitmap = BitmapManager.getBitmapFromCache(tag);
        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(tag, bitmap);
        }
        return bitmap;
    }

    public static Bitmap decodeFile(String path) {
        Bitmap bitmap = BitmapManager.getBitmapFromCache(path);
        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(path, bitmap);
        }
        return bitmap;
    }

    public static Bitmap decodeFile(String path, int width, int height) {
        Bitmap bitmap = BitmapManager.getBitmapFromCache(path);
        if (bitmap == null || bitmap.isRecycled()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = width;
            options.outHeight = height;
            bitmap = BitmapFactory.decodeFile(path, options);
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(path, bitmap);
        }
        return bitmap;
    }

    public static Bitmap decodeFile(String path, BitmapFactory.Options options) {
        Bitmap bitmap = BitmapManager.getBitmapFromCache(path);
        if (bitmap == null || bitmap.isRecycled()) {
            BitmapManager.removeBitmap(path);
            bitmap = BitmapFactory.decodeFile(path, options);
            if (bitmap == null) {
                return null;
            }
            BitmapManager.addBitmapToCache(path, bitmap);
        }
        return bitmap;
    }
}
