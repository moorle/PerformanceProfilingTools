package com.lanshon.strictmode;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private static final int RC_CAMERA_PERM = 101;

    private static List<JSONObject> sData = new ArrayList<>();
    private static List<String> sStringData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (true) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyDialog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .setClassInstanceLimit(JSONObject.class, 2)
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void mount() {
        String[] perms = {Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permission, do the thing!
            Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show();
        } else {
            EasyPermissions.requestPermissions(this, "读写sdcard权限",
                    RC_CAMERA_PERM, perms);
        }
    }

    public void readAssets(View view) {
        JSONObject data = AssetUtils.loadJSONAsset(this, "generated-3.json");
        sData.add(data);
    }

    public void writeDisk(View view) {
        mount();
        for (int i = sData.size() - 1 ; i >= 0; i--) {

            FileUtils.write(this, "file-" + i, sData.get(i).toString());
            StorageUtils.writeToExternalStorage("strictmode/file" + i, sData.get(i).toString());
        }
    }

    public void readDisk(View view) {
        File externalStorage = Environment.getExternalStorageDirectory();
        File filePath = new File(externalStorage, "strictmode/");
        for(String name : filePath.list()) {
            File path = new File(filePath, name);
            String resutl = StorageUtils.readFromExternalStorage(path.getAbsolutePath());
            if (resutl != null) {
                sStringData.add(resutl);
            }
        }


    }

    public void networkRequest(View view) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://www.android.com/");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
