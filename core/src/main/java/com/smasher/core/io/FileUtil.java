package com.smasher.core.io;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;


/**
 * @author moyu
 */
public class FileUtil {

    private final static char[] fPath = {'/', ':', '?', '*', '|', '<', '>', '\\', '"',};

    public static boolean saveFile(File file, String content) {
        return saveFile(file, content, "UTF-8");
    }

    public static boolean saveFile(File file, String content, String charset) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = content.getBytes(charset);
            fos.write(buffer);
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveFile(File file, byte[] data) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveFile(File file, InputStream is) {
        OutputStream os = null;
        try {
            try {
                byte[] fileReader = new byte[1024 * 1024];
                long fileSizeDownloaded = 0;
                os = new FileOutputStream(file);
                while (true) {
                    int read = is.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    os.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                os.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveFile(Bitmap bm, String fileName) {
        try {
            boolean success = false;
            File dirFile = new File(fileName);
            if (dirFile.exists()) {
                dirFile.delete();
            }
            File myCaptureFile = createFile(fileName, true);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            success = bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return success;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static String loadFile(File file) {
        try {
            return loadFile(file, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadFile(File file, String charset) {
        try {
            byte[] bytes = loadFileBytes(file);
            if (bytes != null) {
                return new String(bytes, charset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] loadFileBytes(File file) {
        if (file == null || !file.exists()) {
            return null;
        }

        InputStream is = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] result = null;
        try {
            is = new FileInputStream(file);

            byte[] charBuffer = new byte[1024 * 8];
            int readSize = 0;
            while ((readSize = is.read(charBuffer)) > 0) {
                output.write(charBuffer, 0, readSize);
            }
            output.flush();
            result = output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                output.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static byte[] loadAsset(Context ctx, String path) {
        InputStream is;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] result = null;
        try {
            if (ctx == null) {
                return null;
            }
            if (ctx.getResources() == null) {
                return null;
            }
            if (ctx.getResources().getAssets() == null) {
                return null;
            }
            is = ctx.getResources().getAssets().open(path);

            byte[] charBuffer = new byte[1024 * 8];
            int readSize = 0;
            try {
                while ((readSize = is.read(charBuffer)) > 0) {
                    output.write(charBuffer, 0, readSize);
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            output.flush();
            result = output.toByteArray();
            is.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void copyFile(File sourceFile, File targetFile, boolean overwrite) {
        if (!sourceFile.exists()) {
            return;
        }
        if (targetFile.exists()) {
            if (overwrite) {
                targetFile.delete();
            } else {
                return;
            }
        } else {
            createFile(targetFile, true);
        }
        try {
            InputStream is = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > -1) {
                fos.write(buffer);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFile(String oldPath, String newPath) throws IOException {

        InputStream is = null;
        FileOutputStream fos = null;

        try {
            int byteSum = 0;
            int byteRead = 0;


            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                is = new FileInputStream(oldPath);
                fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteRead = is.read(buffer)) != -1) {
                    byteSum += byteRead;
                    fos.write(buffer, 0, byteRead);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }

            if (fos != null) {
                fos.close();
            }
        }
    }

    public static void copyFile(InputStream is, File targetFile) {
        if (targetFile.exists()) {
            return;
        } else {
            createFile(targetFile, true);
        }
        try {
            FileOutputStream fos = new FileOutputStream(targetFile);
            int byteSum = 0;
            int byteRead = 0;
            byte[] buffer = new byte[1444];
            while ((byteRead = is.read(buffer)) != -1) {
                byteSum += byteRead;
                fos.write(buffer, 0, byteRead);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyAssetsFile(Context ctx, String assetsPath, File targetFile) {
        InputStream is = null;
        try {
            is = ctx.getResources().getAssets().open(assetsPath);
            copyFile(is, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static File createFile(String fileName, boolean isFile) {
        File file = new File(fileName);
        if (file.exists() && isFile) {
            file.delete();
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                createFile(file.getParentFile(), false);
            } else {
                if (isFile) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    file.mkdirs();
                }
            }
        }
        return file;
    }

    public static void createFile(File file, boolean isFile) {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                createFile(file.getParentFile(), false);
            } else {
                if (isFile) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    file.mkdirs();
                }
            }
        }
    }

    public static boolean deleteFile(String fileName) {
        boolean res = false;
        if (!TextUtils.isEmpty(fileName)) {
            File file = new File(fileName);
            if (file.exists()) {
                res = file.delete();
            }
        }
        return res;
    }

    public static boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        boolean res = false;
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i].getAbsolutePath(), true);
                }
                res = true;
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                    res = true;
                } else {
                    if (file.listFiles().length == 0) {
                        file.delete();
                        res = true;
                    }
                }
            }
        }
        return res;
    }

    public static void deleteAllFiles(File root) {
        if (root.exists()) {
            String deleteCmd = "rm -r " + root.getAbsolutePath();
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void moveAllFiles(String sourceFile, String destFile) {
        if (new File(sourceFile).exists()) {
            String deleteCmd = "rename " + sourceFile + " " + destFile;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File findFirstFileByExtension(File dir, final String extension) {
        if (!dir.exists()) {
            return null;
        }
        if (!dir.isDirectory()) {
            return null;
        }
        File[] files = dir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String fname) {
                return fname != null && fname.toLowerCase().endsWith(extension);
            }
        });

        if (files == null || files.length == 0) {
            return null;
        }

        return files[0];
    }

    public static File[] getFilesFromDir(String dirName) {
        File file = new File(dirName);
        if (file.exists()) {
            File[] files = file.listFiles();
            return files;
        }
        return null;
    }

    public static int readLittleEndianInt(InputStream dis) throws IOException {
        byte[] bytes = new byte[4];
        int count = dis.read(bytes);
        ByteBuffer bytebuffer = ByteBuffer.wrap(bytes);
        bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
        return bytebuffer.getInt();
    }

    public static long readLittleEndianLong(InputStream dis) throws IOException {
        byte[] bytes = new byte[8];
        int re = dis.read(bytes);
        if (re == -1) {
            return -1;
        }
        ByteBuffer bytebuffer = ByteBuffer.wrap(bytes);
        bytebuffer.order(ByteOrder.LITTLE_ENDIAN);
        long result = bytebuffer.getLong();
        return result;
    }

    public static void createNoMeida(String dir) {
        File file = new File(dir + ".nomedia");
        if (file.exists()) {
            return;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void scanDirAsync(Context ctx, String dir) {
        Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
        scanIntent.setData(Uri.fromFile(new File(dir)));
        ctx.sendBroadcast(scanIntent);
    }

    public static void copyAllFile(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File a = new File(oldPath);
            String[] file = a.list();
            if (file == null) {
                return;
            }
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1444];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copyAllFile(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String checkPath(StringBuffer filePath) {
        for (int i = 0; i < filePath.length(); i++) {
            for (int j = 0; j < fPath.length; j++) {
                if (filePath.charAt(i) == fPath[j]) {
                    filePath.deleteCharAt(i);
                    break;
                }
            }
        }
        return filePath.toString();
    }

    /**
     * 获取指定路径下的文件
     *
     * @param access access
     * @return File
     */
    public static File getAccessFileOrCreate(String access) {
        try {
            File f = new File(access);
            if (!f.exists()) {
                if (mkdirsIfNotExit(f.getParentFile())) {
                    f.createNewFile();
                }
                return f;
            } else {
                return f;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean mkdirsIfNotExit(File f) {
        if (f == null) {
            return false;
        }
        if (!f.exists()) {
            synchronized (FileUtil.class) {
                return f.mkdirs();
            }
        }
        return true;
    }

    /**
     * 保存bitmap
     *
     * @param targetPath targetPath
     * @param fileName   fileName
     * @param bitmap     bitmap
     */
    public static void saveBitmap(String targetPath, String fileName, Bitmap bitmap) {
        File f = new File(targetPath, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * StreamToString
     *
     * @param is is
     * @return String
     */
    public static String convertStreamToString(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static boolean appendFile(File file, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            byte[] buffer = content.getBytes();
            fos.write(buffer);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void makeDir(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * gzip压缩文件
     *
     * @param fromFile fromFile
     * @param toFile   toFile
     */
    public static void gzipFile(File fromFile, File toFile) {
        try {
            if (fromFile.exists()) {
                GZIPOutputStream out = null;
                if (!toFile.exists()) {
                    toFile.createNewFile();
                }
                out = new GZIPOutputStream(new FileOutputStream(toFile));
                FileInputStream in = new FileInputStream(fromFile);
                byte[] buf = new byte[1024];
                int len;
                if (in != null) {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                }
                if (out != null) {
                    out.finish();
                    out.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
