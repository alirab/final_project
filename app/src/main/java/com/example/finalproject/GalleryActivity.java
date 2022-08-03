package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;



    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        //drawer layout actions
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string, R.string.close_string);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //nav view item select listener, utilizing switch case to initialize activities using intent filter
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.home:
                    Intent m = new Intent(GalleryActivity.this, MainActivity.class);
                    startActivity(m);
                    break;
                case R.id.photos:
                    Toast.makeText(this, "You are already in the gallery.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.help:
                    Intent a = new Intent(GalleryActivity.this, HelpActivity.class);
                    startActivity(a);
                    break;

            }
            return true;
        });

    }

    //toolbar icons menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_icons, menu);
        return true;
    }
    //switch case generating toast if item(n) is selected
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent m = new Intent(GalleryActivity.this, MainActivity.class);
                startActivity(m);
                return true;
            case R.id.item2:
                Toast.makeText(this, "You are already in the gallery.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item3:
                Intent a = new Intent(GalleryActivity.this, HelpActivity.class);
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}