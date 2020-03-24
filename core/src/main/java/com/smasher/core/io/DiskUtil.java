package com.smasher.core.io;

import android.os.Environment;
import android.os.StatFs;

import com.smasher.core.R;
import com.smasher.core.other.ApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class DiskUtil {
    /*获取文件大小单位为B的double值*/
    public static final int SIZE_TYPE_B = 1;
    /*获取文件大小单位为KB的double值*/
    public static final int SIZE_TYPE_KB = 2;
    /* 获取文件大小单位为MB的double值*/
    public static final int SIZE_TYPE_MB = 3;
    /* 获取文件大小单位为GB的double值*/
    public static final int SIZE_TYPE_GB = 4;

    public static final long limit = 30L;

    public static boolean checkSDCard() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static String checkDisk() {
        long internalAvailable = getAvailableSize(ApplicationContext.getInstance().getFilesDir().getAbsolutePath());
        if (internalAvailable < limit) {
            return ApplicationContext.getInstance().getString(R.string.no_sd_space);
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // TODO: 2020/3/24 0024 modify filepath
            //AppPath.getRootPath()
            String path = ApplicationContext.getInstance().getFilesDir().getPath();
            long sdcardAvailable = getAvailableSize(path);
            if (sdcardAvailable < 30) {
                return ApplicationContext.getInstance().getString(R.string.no_sd_space);
            }
        } else {
            return ApplicationContext.getInstance().getString(R.string.sd_error);
        }
        return null;
    }

    public static long getAvailableSize(String path) {
        try {
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            //MB
            return (availableBlocks * blockSize) / 1024 / 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 获取指定文件夹
     *
     * @param f f
     * @return long
     * @throws Exception Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                size = size + getFileSizes(files[i]);
            } else {
                size = size + getFileSize(files[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     *
     * @return long
     * @throws Exception Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        FileInputStream fis = null;
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileSize fileSize
     * @return String
     */
    private static String formatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileSize == 0) {
            return wrongSize;
        }
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileSize fileSize
     * @param sizeType sizeType
     * @return double
     */
    private static double formatFileSize(long fileSize, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZE_TYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileSize));
                break;
            case SIZE_TYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileSize / 1024));
                break;
            case SIZE_TYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileSize / 1048576));
                break;
            case SIZE_TYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileSize / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
}
