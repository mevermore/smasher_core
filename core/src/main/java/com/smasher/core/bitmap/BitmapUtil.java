package com.smasher.core.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;

import com.smasher.core.log.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * BitmapUtil
 *
 * @author moyu
 * @date 2017/4/10
 */
public class BitmapUtil {

    /**
     * 算缩放值
     *
     * @param options   options
     * @param reqWidth  reqWidth
     * @param reqHeight reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * 解析大图片
     *
     * @param filename  filename
     * @param reqWidth  reqWidth
     * @param reqHeight reqHeight
     * @return Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return DeBitmapFactory.decodeFile(filename, options);
    }

    /**
     * 获得圆角图片的方法
     */
    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, String tag, float roundPx) {
        try {
            Bitmap output = DeBitmapFactory.createBitmap(tag, bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff000000;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Drawable 转成 Bitmap
     *
     * @param drawable drawable
     * @return Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        try {
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path path
     * @return int
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            Logger.exception(e);
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param bitmap bitmap
     * @param degree degree
     * @return Bitmap
     */
    public static Bitmap rotateBitmapByDegree(String tag, Bitmap bitmap, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = DeBitmapFactory.createBitmap("", bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            Logger.exception(e);
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /***
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap getGrayBitmap(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        if (!bmpOriginal.isRecycled()) {
            c.drawBitmap(bmpOriginal, 0, 0, paint);
        }
        return bmpGrayScale;
    }

    /**
     * 压缩图片
     *
     * @param imgPath imgPath
     * @return Bitmap
     */
    public static Bitmap compressBitmap(String imgPath, int width, int height) {
        Bitmap srcBitmap = DeBitmapFactory.decodeFile(imgPath, width, height);
        if (srcBitmap == null || srcBitmap.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            //重置baos即清空baos
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //每次都减少10
            options -= 10;
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * 创建一个纯色的图片
     *
     * @param w     w
     * @param h     h
     * @param color color
     * @return Bitmap
     */
    public static Bitmap createColorBitmap(int w, int h, int color) {
        int[] colors = new int[w * h];
        Arrays.fill(colors, color);
        return Bitmap.createBitmap(colors, w, h, Bitmap.Config.ARGB_8888);
    }

    /**
     * 创建一个带有蒙版效果的Bitmap
     *
     * @param bmp   bmp
     * @param color color
     * @return Bitmap
     */
    public static Bitmap getColorMaskedBitmap(Bitmap bmp, int color) {
        if (bmp == null) {
            return null;
        }

        Bitmap copyBitmap = null;
        try {
            if (!bmp.isRecycled()) {
                copyBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap maskBitmap = getMaskBitmap(copyBitmap, color);
                if (maskBitmap != null && !maskBitmap.isRecycled()) {
                    Canvas canvas = new Canvas(copyBitmap);
                    canvas.drawBitmap(maskBitmap, 0, 0, null);
                }
                if (maskBitmap != null) {
                    maskBitmap.recycle();
                }
            }
        } catch (Exception | OutOfMemoryError e) {
            e.printStackTrace();
        }
        return copyBitmap;
    }

    /**
     * 创建一个带有蒙版效果的Bitmap
     *
     * @param bmp   bmp
     * @param color color
     * @return Bitmap
     */
    public static Bitmap getMaskBitmap(Bitmap bmp, int color) {
        if (bmp == null) {
            return null;
        }
        Bitmap resultBmp = null;
        try {
            //前置相片添加蒙板效果
            int w = bmp.getWidth();
            int h = bmp.getHeight();
            int[] picPixels = new int[w * h];
            int[] maskPixels = new int[w * h];
            Bitmap maskColorBitmap = createColorBitmap(w, h, color);
            bmp.getPixels(picPixels, 0, w, 0, 0, w, h);
            maskColorBitmap.getPixels(maskPixels, 0, w, 0, 0, w, h);
            for (int i = 0; i < picPixels.length; i++) {
                if (picPixels[i] == 0) {
                    //设置为透明
                    maskPixels[i] = 0;
                }
            }
            resultBmp = Bitmap.createBitmap(maskPixels, w, h, Bitmap.Config.ARGB_8888);
            //回收蒙版Bitmap
            if (maskColorBitmap != null) {
                maskColorBitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return resultBmp;
    }

    public static Uri getImagePathUri(String path) {
        return Uri.parse("file://" + path);
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        byte[] bytes = null;
        if (bm != null && !bm.isRecycled()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bytes = baos.toByteArray();
        }
        return bytes;
    }
}
