package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    GridView gallery;
    TextView help;

    private ArrayList<String> images;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);


        gallery = findViewById(R.id.galleryGridView);

        gallery.setAdapter(new ImageAdapter(this));


        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(null != images && !images.isEmpty()){
                    Toast.makeText(getApplicationContext(),images.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        });


        help = findViewById(R.id.textViewHelp);
        //help button
        help.setOnClickListener(view -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(help.getContext());
                    alert.setTitle(R.string.help).setMessage(R.string.gallery_help)
                            .setPositiveButton(getString(R.string.okay), (dialog, which) -> {

                            });
            alert.show();
                });

        //SharedPrefs

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
                case R.id.randomizer:
                    Intent r = new Intent(GalleryActivity.this, NasaPhotoActivity.class);
                    startActivity(r);
                    break;
                case R.id.photos:
                    Toast.makeText(this, getString(R.string.already_gallery), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.help:
                    Intent a = new Intent(GalleryActivity.this, HelpActivity.class);
                    startActivity(a);
                    break;

            }
            return true;
        });

    }

    private class ImageAdapter extends BaseAdapter{

        private Activity context;

        public ImageAdapter(Activity localContext){
            context = localContext;
            images = getAllShownImagesPath(context);
        }
        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {

            ImageView imageView;
            if(view == null){
                imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(500,500));
            }else{
                imageView = (ImageView) view;
            }
            Glide.with(context).load(images.get(position)).placeholder(R.drawable.ic_launcher_foreground).centerCrop().into(imageView);
            return imageView;
        }
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;

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
                Toast.makeText(this, getString(R.string.already_gallery), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item4:
                Intent r = new Intent(GalleryActivity.this, NasaPhotoActivity.class);
                startActivity(r);
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