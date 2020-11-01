package com.example.calorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {

    // Camera and Barcode data
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private String barcodeData;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    // UI elements
    private SurfaceView surfaceView;
    private TextView barcodeText;

    // Home Button
    private ImageView imHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        // The view of the camera and the text that shows what the barcode reads
       surfaceView = (SurfaceView) findViewById(R.id.barcode_view);
       barcodeText = (TextView) findViewById(R.id.barcode_text);

        createDetectorsAndSources(); // The functionality of the barcode

        imHome = findViewById(R.id.home_image);
        imHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomeActivity();
            }
        });
    }

    protected void goToHomeActivity()
    {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    private void createDetectorsAndSources() {

        // The barcode detector
        barcodeDetector = new BarcodeDetector.Builder(this).
                setBarcodeFormats(Barcode.ALL_FORMATS).build();

        // The camera source
        cameraSource = new CameraSource.Builder(this, barcodeDetector).
                setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();

        // Sets up the surface view in which we can see the camera used for scanning
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {

                try {
                    // Grant permission to the camera
                    if (ActivityCompat.checkSelfPermission(BarcodeActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder()); // Puts the camera in the surfaceView
                    } else {
                        ActivityCompat.requestPermissions(BarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop(); // Stop the camera capturing
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            // Takes the value read by the barcode, and puts it into the TextView
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if (barcode.size() != 0) {

                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcode.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcode.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                            } else {
                                barcodeData = barcode.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                            }

                        }
                    });
                }
            }
        });
    }
}