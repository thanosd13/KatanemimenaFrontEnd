package com.example.airbnb_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airbnb_app.adapter.RecentsAdapter;
import com.example.airbnb_app.adapter.TopPlacesAdapter;
import com.example.airbnb_app.databinding.ActivityMainBinding;
import com.example.airbnb_app.model.RecentsData;
import com.example.airbnb_app.model.TopPlacesData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
            replaceFragment(new HomeFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {


            if (menuItem.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            }

            if (menuItem.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());
            }

            return true;
        });

    }

    private void replaceFragment (Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main, fragment);
        fragmentTransaction.commit();

    }



}