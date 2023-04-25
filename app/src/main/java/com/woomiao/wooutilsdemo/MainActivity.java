package com.woomiao.wooutilsdemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.woomiao.wooutilsdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.amountKeyboardBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            intent.putExtra("name", "amountKey");
            startActivity(intent);
        });

        binding.customSpinnerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            intent.putExtra("name", "customSpinner");
            startActivity(intent);
        });

        binding.pageNumBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            intent.putExtra("name", "pageNum");
            startActivity(intent);
        });

        binding.tableviewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            intent.putExtra("name", "tableView");
            startActivity(intent);
        });

        binding.otherBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            intent.putExtra("name", "other");
            startActivity(intent);
        });
    }
}