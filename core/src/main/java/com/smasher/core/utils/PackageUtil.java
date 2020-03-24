package com.smasher.core.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PackageUtil {

    public static int checkPackageInstalled(Context ctx, String packageName) {
        PackageManager packageManager = ctx.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return 1;
        }
        return 0;
    }

    public static void startPackage(Activity ctx, String packageName, String query) {
        PackageInfo pi = null;
        try {
            pi = ctx.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = ctx.getPackageManager().queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String packageName1 = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName cn = new ComponentName(packageName1, className);
            intent.setComponent(cn);

            //解析params
            //name=xxx&i.type=2&l.time=1111111&b.isLogin=true
            Map<String, String> map = UrlUtil.getQueryString(query);
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String key = entry.getKey();
                String val = entry.getValue();

                String[] keyArray = key.split("\\.");
                if (keyArray.length > 1) {
                    if ("I".equalsIgnoreCase(keyArray[0])) {
                        // 是int类型
                        intent.putExtra(keyArray[1], Integer.valueOf(val));
                    } else if ("L".equalsIgnoreCase(keyArray[0])) {
                        // 是long类型
                        intent.putExtra(keyArray[1], Long.valueOf(val));
                    } else if ("B".equalsIgnoreCase(keyArray[0])) {
                        // 是String类型
                        intent.putExtra(keyArray[1], Boolean.parseBoolean(val));
                    } else {
                        // 是String类型
                        intent.putExtra(keyArray[1], val);
                    }
                }
            }
            ctx.startActivity(intent);
        }
    }

    /**
     * 获取签名信息
     *
     * @param ctx ctx
     */
    public static void getSignatureInfo(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            Signature signature = signatures[0];
            parseSignature(signature.toByteArray());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param signature signature
     */
    private static void parseSignature(byte[] signature) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(signature));
            byte[] buff = certificate.getEncoded();
            String pubKey = certificate.getPublicKey().toString();
            String signNumber = certificate.getSerialNumber().toString();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取版本号
     *
     * @param ctx ctx
     * @return int
     */
    public static int getVersionCode(Context ctx) {
        PackageInfo pi = null;
        try {
            PackageManager pm = ctx.getPackageManager();
            pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            return pi == null ? 0 : pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 是否是debug包
     *
     * @param context context
     * @return boolean
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 获取所有Activity名称
     *
     * @param ctx ctx
     * @return ArrayList
     */
    public static ArrayList getActivities(Context ctx) {
        try {
            ArrayList<String> result = new ArrayList<>();
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.setPackage(ctx.getPackageName());
            for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(intent, 0)) {
                result.add(info.activityInfo.name);
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
