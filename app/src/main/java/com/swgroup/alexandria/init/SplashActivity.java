package com.swgroup.alexandria.init;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.swgroup.alexandria.MainActivity;
import com.swgroup.alexandria.R;
import com.swgroup.alexandria.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        };

        Runnable fadingEffect = new Runnable() {
            @Override
            public void run() {
                fade();
            }
        };

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(fadingEffect);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    public void fade(){
        binding.splashLogo.startAnimation(
                AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.fade_in));
    }

}
