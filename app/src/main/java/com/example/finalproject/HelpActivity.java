package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class HelpActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView textView;
    TextView help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        textView = findViewById(R.id.textView);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch(item.getItemId()){
                case R.id.home:
                   Intent h = new Intent(HelpActivity.this, MainActivity.class);
                   startActivity(h);
                    break;
                case R.id.randomizer:
                    Intent r = new Intent(HelpActivity.this, NasaPhotoActivity.class);
                    startActivity(r);
                    break;
                case R.id.photos:
                    Intent i = new Intent(HelpActivity.this, GalleryActivity.class);
                    startActivity(i);
                    break;
                case R.id.help:
                    Toast.makeText(this, getString(R.string.already_help), Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string, R.string.close_string);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        help = findViewById(R.id.textViewHelp);

        help.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(help.getContext());
            alert.setTitle(R.string.help).setMessage(R.string.help_help)
                    .setPositiveButton(getString(R.string.okay), (dialog, which) -> {

                    });
            alert.show();
        });
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_icons, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent h = new Intent(HelpActivity.this,MainActivity.class);
                startActivity(h);
            case R.id.item4:
                    Intent r = new Intent(HelpActivity.this, NasaPhotoActivity.class);
                    startActivity(r);
                return true;
            case R.id.item2:
                Intent i = new Intent(HelpActivity.this, GalleryActivity.class);
                startActivity(i);
                return true;
            case R.id.item3:
                Toast.makeText(this, getString(R.string.already_help), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}