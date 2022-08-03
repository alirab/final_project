package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Browser;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
    Button save;
    Button reroll;
    ImageView imageView;
    Bitmap bitmap;
    TextView title;
    TextView date;
    TextView descURL;
    ProgressBar progressBar;
    TextView help;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nasa_photo);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);

        save = findViewById(R.id.button1); //save

        //request permission to read and write
        ActivityCompat.requestPermissions(NasaPhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(NasaPhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);

        reroll = findViewById(R.id.button2); //re-roll

        //tool bar support
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //declaring text views
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);

        //make url clickable
        descURL = findViewById(R.id.URL);

        imageView = findViewById(R.id.imageView);

        help = findViewById(R.id.textViewHelp);

        //declaring async class
        NASAImage nasaImage = new NASAImage();
        nasaImage.execute();

        progressBar = findViewById(R.id.progressBar);

        //drawer layout actions
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_string, R.string.close_string);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Save  image function ->
        save.setOnClickListener(view -> {
            saveToGallery();



            Snackbar snackbar = Snackbar.make(view,"Image downloaded successfully.",Snackbar.LENGTH_LONG);
            snackbar.setDuration(20000);
            snackbar.setAction("OKAY", new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                //can do something
                }
            });
            snackbar.show();
        });

        //help menu option
        help.setOnClickListener(view -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(help.getContext());
            alert.setTitle("Help").setMessage("Insert instructions here...");
            alert.show();
        });

        //Re-roll function is working -> new thread created on the async task to handle multiple thread executions
        reroll.setOnClickListener(view -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(reroll.getContext());
            alert.setTitle("Are you sure you want to re-roll this image?")
                    .setMessage("New random NASA image?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        NASAImage newNasaImage = new NASAImage();
                        newNasaImage.execute();

                    }).setNegativeButton("No", (dialog, which) -> {
                        dialog.cancel();
                    });
            alert.show();

        });

        //nav view item select listener, utilizing switch case to initialize activities using intent filter
        navigationView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (item.getItemId()) {
                case R.id.home:
                    Intent c = new Intent(NasaPhotoActivity.this, MainActivity.class);
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

    class NASAImage extends AsyncTask<String, Integer, Bitmap> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setProgress(values[0]);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Bitmap doInBackground(String... objects) {

            try {
                String url = "https://api.nasa.gov/planetary/apod?api_key=cwZOfj9H4q0nNRzepGBmHAhICE2L7XRPOeNxHlrK&date=" + generateRandomDate();
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String json = bufferedReader.readLine();
                JSONObject jsonObject = new JSONObject(json);

                String imageURL = jsonObject.getString("hdurl");
                URL nasaURL = new URL(imageURL);
                descURL.setText(imageURL);

                //URL onclick listener
                descURL.setOnClickListener(view -> {

                    Context context = descURL.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(imageURL));

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.android.chrome");
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID,context.getPackageName());
                    try{
                        startActivity(intent);
                    }catch(ActivityNotFoundException ex){

                        intent.setPackage(null);
                        startActivity(Intent.createChooser(intent, "Select Browser"));
                    }
                });

                String imageDate = jsonObject.getString("date");
                date.setText("Date: " + imageDate);

                String imageTitle = jsonObject.getString("title");
                title.setText("Title: " + imageTitle);

                HttpURLConnection httpURLConnection1 = (HttpURLConnection) nasaURL.openConnection();
                httpURLConnection1.setDoInput(true);
                httpURLConnection1.connect();

                InputStream inputStream1 = httpURLConnection1.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream1);

            } catch (IOException | JSONException ioe) {
                ioe.printStackTrace();
            }
            for (int i = 0; i < 15; i++) {
                try {
                    publishProgress(i);
                    Thread.sleep(1);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            publishProgress(0);
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(imageView != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }
                new NASAImage();
        }
    }

    //Save to gallery function using bitmap drawable and file output stream
    private void saveToGallery(){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath()+"/NASAImages");
        dir.mkdirs();

        @SuppressLint("DefaultLocale")
        String filename = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(dir,filename);

        try{
            outputStream = new FileOutputStream(outFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try{
            outputStream.flush();
        }catch(Exception e){
            e.printStackTrace();
        }try {
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //generating a random date --> using method to generate image in Async
    private String generateRandomDate() {

        Calendar calendar = Calendar.getInstance();
        int year = randomBetween(1995, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.YEAR, year);
        int day = randomBetween(1, calendar.get(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.DAY_OF_YEAR, day);
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static int randomBetween(int start, int end) {
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
                Intent c = new Intent(NasaPhotoActivity.this, MainActivity.class);
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