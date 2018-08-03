package com.gswstudio.senteri;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnSwitch;

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private Camera.Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);


        btnSwitch = findViewById(R.id.btnSwitch);
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Tidak Support Flash Light");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
            return;
        }
        getCamera();
        toggleButtonImage();
        btnSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlash();
                } else {
                    turnOnFlash();
                }
            }
        });
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1: {
//                if (grantResults.length <= 0
//                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Permission camera gagal", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    @SuppressLint("LongLogTag")
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    /*
     * Turning On flash
     */
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            toggleButtonImage();
        }

    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            toggleButtonImage();
        }
    }

    private void toggleButtonImage() {
        if (isFlashOn) {
            btnSwitch.setImageResource(R.drawable.ic_light_on);
        } else {
            btnSwitch.setImageResource(R.drawable.ic_light_off);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasFlash)
            turnOnFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
