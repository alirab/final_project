package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class NasaPhotoActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button button1;
    Button button2;
    ImageView imageView;
    Bitmap bitmap;
    TextView title;
    TextView date;
    TextView descURL;


    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nasa_photo);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);
        button1 =  findViewById(R.id.button1); //save
        button2 = findViewById(R.id.button2); //re-roll

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        date.setText("Date: "+generateRandomDate());
        descURL = findViewById(R.id.URL);
        imageView = findViewById(R.id.imageView);

        //declaring async class
        NASAImage nasaImage = new NASAImage();
        nasaImage.execute();


        //drawer layout actions
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string, R.string.close_string);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        button2.setOnClickListener(view -> {
                date.setText("Date: " + generateRandomDate());
        });


        //nav view item select listener, utilizing switch case to initialize activities using intent filter
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch(item.getItemId()){
                case R.id.home:
                    Intent c =  new Intent (NasaPhotoActivity.this, MainActivity.class);
                    startActivity(c);
                    break;
                case R.id.photos:
                    Intent i = new Intent(NasaPhotoActivity.this, GalleryActivity.class);
                    startActivity(i);
                    break;
                case R.id.help:
                    Intent a = new Intent(NasaPhotoActivity.this, HelpActivity.class);
                    startActivity(a);
                    break;

            }
            return true;
        });

    }

    class NASAImage extends AsyncTask<String, Void, Bitmap> {

        protected void onPreExecute(){
            super.onPreExecute();
        }

        protected void onProgressUpdate(Void...voids){}

        @Override
        protected Bitmap doInBackground(String... objects) {
            try{
                String url = "https://api.nasa.gov/planetary/apod?api_key=cwZOfj9H4q0nNRzepGBmHAhICE2L7XRPOeNxHlrK&date="+generateRandomDate();
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String json = bufferedReader.readLine();
                JSONObject jsonObject = new JSONObject(json);

                String imageURL = jsonObject.getString("hdurl");
                URL nasaURL = new URL(imageURL);
                descURL.setText("URL: "+imageURL);

                String imageDate = jsonObject.getString("date");
                date.setText("Date: "+imageDate);

                String imageTitle = jsonObject.getString("title");
                title.setText("Title: "+imageTitle);

                HttpURLConnection httpURLConnection1 = (HttpURLConnection) nasaURL.openConnection();
                httpURLConnection1.setDoInput(true);
                httpURLConnection1.connect();

                InputStream inputStream1 = httpURLConnection1.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream1);



            }catch (IOException | JSONException ioe){
                ioe.printStackTrace();
            }
            return bitmap;

        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (imageView != null){
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }
            new NASAImage().execute();
        }

    }

    //generating a random date --> using method to generate image
    private String generateRandomDate(){

        Calendar calendar = Calendar.getInstance();
        int year = randomBetween(1995,calendar.get(Calendar.YEAR));
        calendar.set(Calendar.YEAR, year);
        int day = randomBetween(1, calendar.get(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.DAY_OF_YEAR, day);
        return calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static int randomBetween(int start, int end){
        return start + (int) Math.round(Math.random() * (end - start));
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
                Intent c =  new Intent (NasaPhotoActivity.this, MainActivity.class);
                startActivity(c);
                return true;
            case R.id.item2:
                Intent i = new Intent(NasaPhotoActivity.this, GalleryActivity.class);
                startActivity(i);
                return true;

            case R.id.item3:
                Intent a = new Intent(NasaPhotoActivity.this, HelpActivity.class);
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}