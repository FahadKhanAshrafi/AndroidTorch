package com.example.fahadkhanashrafi.flashytorch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    ImageView flashLightButton;
    AdView adView;
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;
    private Camera camera;
    private Camera.Parameters parameters;
    boolean isFlashLightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flashLightButton = (ImageView) findViewById(R.id.torchAction);
        adView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1){

        }

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.banner_ad_unit_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                // Call displayInterstitial() function
                displayInterstitial();
            }
        });

        /**/
        /*
        flashLightButton.setImageResource(R.drawable.on);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
        isFlashLightOn = true;
        */
        /**/

        flashLightButton.setOnClickListener(new  FlashOnOffListener());

        if (isFlashSupported()) {
            camera = Camera.open();
            parameters = camera.getParameters();
        } else {
            showNoFlashAlert();
        }
    }

        class FlashOnOffListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                if (isFlashLightOn) {
                    flashLightButton.setImageResource(R.drawable.off);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    isFlashLightOn = false;
                } else {
                    flashLightButton.setImageResource(R.drawable.on);
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    isFlashLightOn = true;
                }

            }

        }

    public void displayInterstitial()
    {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


    private void showNoFlashAlert() {
        new AlertDialog.Builder(this)
                .setMessage("Your device hardware does not support flashlight!")
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle("Error")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    private boolean isFlashSupported() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    protected void onDestroy() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        super.onDestroy();
    }
}
