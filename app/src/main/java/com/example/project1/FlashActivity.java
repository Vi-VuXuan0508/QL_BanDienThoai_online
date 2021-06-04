package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.project1.kien.Login.LoginActivity;

public class FlashActivity extends AppCompatActivity {
    final private int PERMISSION_ALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        String[] PERMISSIONS = {
                Manifest.permission.INTERNET,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,

                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if (!hasPermission(FlashActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(FlashActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();

    }
    public static boolean hasPermission(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}