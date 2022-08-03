package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button button;
    TextView help;
    TextView intro;
    EditText firstName;
    EditText lastName;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String F_TEXT = "fText";
    public static final String L_TEXT = "lText";

    private String fText;
    private String lText;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);
        button =  findViewById(R.id.button);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        intro = findViewById(R.id.intro);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);


        //drawer layout actions
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string, R.string.close_string);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //help
        help = findViewById(R.id.textViewHelp);

        help.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(help.getContext());
            alert.setTitle(R.string.help).setMessage(R.string.main_help)
                    .setPositiveButton(getString(R.string.okay), (dialog, which) -> {

            });
            alert.show();
        });

        //button to roll for new photo
        button.setOnClickListener(view -> {

            if(firstName.getText().toString().trim().length() < 1  || lastName.getText().toString().trim().length() < 1){
                Toast.makeText(this,getString(R.string.enter_name),Toast.LENGTH_SHORT).show();
            }else{
            Intent n = new Intent(MainActivity.this, NasaPhotoActivity.class);
            startActivity(n);
            }
            saveData();
        });

        //nav view item select listener, utilizing switch case to initialize activities using intent filter
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch(item.getItemId()){
                case R.id.home:
                    Toast.makeText(this, getString(R.string.already_home), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.randomizer:

                    if(firstName.getText().toString().trim().length() < 1  || lastName.getText().toString().trim().length() < 1){
                        Toast.makeText(this,getString(R.string.enter_name),Toast.LENGTH_SHORT).show();
                    }else {

                        Intent r = new Intent(MainActivity.this, NasaPhotoActivity.class);
                        startActivity(r);
                        saveData();
                    }
                    break;
                case R.id.photos:
                    Intent i = new Intent(MainActivity.this, GalleryActivity.class);
                    startActivity(i);
                    break;
                case R.id.help:
                    Intent a = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(a);
                    break;

            }
            return true;
        });
        loadData();
        updateViews();
    }

    //shared prefs
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(L_TEXT, lastName.getText().toString());
        editor.putString(F_TEXT, firstName.getText().toString());

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        fText = sharedPreferences.getString(F_TEXT, "");
        lText = sharedPreferences.getString(L_TEXT, "");
    }

    public void updateViews() {
        firstName.setText(fText);
        lastName.setText(lText);
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
                Toast.makeText(this, getString(R.string.already_home), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item4:

                if(firstName.getText().toString().trim().length() < 1  || lastName.getText().toString().trim().length() < 1){
                    Toast.makeText(this,getString(R.string.enter_name),Toast.LENGTH_SHORT).show();
                }else{
                Intent r = new Intent(MainActivity.this, NasaPhotoActivity.class);
                startActivity(r);
                saveData();
                }
                return true;
            case R.id.item2:
                Intent i = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(i);
                return true;
            case R.id.item3:
                Intent a = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}