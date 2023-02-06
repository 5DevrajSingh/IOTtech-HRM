package com.androidproject.iottechhrm.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidproject.iottechhrm.login.LoginActivity;
import com.androidproject.iottechhrm.R;
import com.androidproject.iottechhrm.util.BaseActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends BaseActivity
{

    RelativeLayout rl_main;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    SharedPreferences pref;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.activity_splash);
        hideToolbar();

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        id = pref.getString("id", null);

        rl_main=findViewById(R.id.rl_main);

        goToNextActivity();

    }







    private void goToNextActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, SPLASH_DISPLAY_LENGTH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withActivity(this).withPermissions(

                            Manifest.permission.READ_MEDIA_IMAGES
                            )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // do you work now


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (TextUtils.isEmpty(id)) {
                                            Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent=new Intent(SplashActivity.this, DashBoardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                }, SPLASH_DISPLAY_LENGTH);


                            }

                            // check for permanent denial of any permission
    //                        if (report.isAnyPermissionPermanentlyDenied()) {
    //                            // permission is denied permenantly, navigate user to app settings
    //                            showSettingsDialog();
    //                        }
                        }

    //                    @Override
    //                    public void onPermissionDenied(PermissionDeniedResponse response) {
    //                        // check for permanent denial of permission
    //                        if (response.isPermanentlyDenied()) {
    //                            showSettingsDialog();
    //                        }
    //                    }


                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(new PermissionRequestErrorListener() {
                @Override
                public void onError(DexterError error) {
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                }
            })
                    .onSameThread()
                    .check();
        }else {
            Dexter.withActivity(this).withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE

                    )
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // do you work now


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (TextUtils.isEmpty(id)) {
                                            Intent intent=new Intent(SplashActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent=new Intent(SplashActivity.this, DashBoardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                }, SPLASH_DISPLAY_LENGTH);


                            }

                            // check for permanent denial of any permission
                            //                        if (report.isAnyPermissionPermanentlyDenied()) {
                            //                            // permission is denied permenantly, navigate user to app settings
                            //                            showSettingsDialog();
                            //                        }
                        }

                        //                    @Override
                        //                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        //                        // check for permanent denial of permission
                        //                        if (response.isPermanentlyDenied()) {
                        //                            showSettingsDialog();
                        //                        }
                        //                    }


                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onSameThread()
                    .check();
        }


    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }



}
