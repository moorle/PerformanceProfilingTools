package com.lanshon.strictmode;

import android.app.Application;
import android.os.StrictMode;

import org.json.JSONObject;

/**
 * Created by lanshon on 18/05/2017.
 */

public class StrictModeApp extends Application {

    private static final boolean DEVELOPER_MODE = true;

    @Override
    public void onCreate() {
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .detectCustomSlowCalls()
                    .detectResourceMismatches()
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectActivityLeaks() // Activity泄露
                    .detectLeakedSqlLiteObjects() // 泄露的Sqlite对象
                    .detectLeakedClosableObjects() // 未关闭的Closable对象泄露
                    .detectCleartextNetwork() // 网络流量监控
                    .detectLeakedRegistrationObjects() // 广播或者服务等未注销导致泄漏
                    .detectFileUriExposure() // 文件uri暴露
                    .setClassInstanceLimit(JSONObject.class, 2) //对象在内存中的最大数
                    .penaltyLog() // 打印到logcat
                    .penaltyDeath()
                    .build());
        }
        super.onCreate();
    }
}
