package com.example.biumi_iot_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.biumi_iot_project.databinding.ActivityLogoBinding;

public class LogoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.biumi_iot_project.databinding.ActivityLogoBinding binding = ActivityLogoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView imageView = (ImageView) binding.logo;

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.drawable.biumi).into(gifImage);

        Handler handler = new Handler();
        handler.postDelayed(this::exit, 5000); //딜레이 타임 조절
    }

    public void exit() {
        startActivity(new Intent(LogoActivity.this, HomeActivity.class));
        finish();
    }
}
