package com.example.mytvs.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytvs.BuildConfig;
import com.example.mytvs.R;
import com.example.mytvs.model.Person;
import com.example.mytvs.utils.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.p_name) TextView nameView;
    @BindView(R.id.p_city) TextView cityView;
    @BindView(R.id.p_date) TextView dateView;
    @BindView(R.id.p_occupation) TextView occupationView;
    @BindView(R.id.pic_time) TextView timeView;
    @BindView(R.id.p_sal) TextView salaryView;
    @BindView(R.id.camera_pic) ImageView cameraView;
    @BindView(R.id.click_click) ImageView imageView;

    private Person person;

    private String mPhotoPath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Setting custom font to toolbar title
        TextView toolText = toolbar.findViewById(R.id.toolbar_detail_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/logo.ttf");
        toolText.setTypeface(custom_font);

        person = getIntent().getParcelableExtra(Utility.INTENT_PERSON);

        cameraView.setOnClickListener(this);
        nameView.setText(person.getName());

        String occupation = "(" + person.getOccupation() + ")";
        occupationView.setText(occupation);

        String location = person.getLocation() + " (" + person.getPinCode() + ")";
        cityView.setText(location);

        dateView.setText(Utility.getDate(person.getDate()));
        salaryView.setText(person.getSalary());

        if(savedInstanceState != null){
            mPhotoPath = savedInstanceState.getString("photoPath");
            Picasso.get()
                    .load(mPhotoPath)
                    .into(imageView);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocation();
            }
        });
    }

    /*
    Method to launch MapActivity
     */
    public void showLocation(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putParcelableArrayListExtra(Utility.INTENT_MAP, Utility.getLatLong(this, person.getLocation()));
        intent.putExtra(Utility.INTENT_LOCATION, person.getLocation());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null){
            // Creating File where the image will go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (IOException e){
                Toast.makeText(this, "Error storing image", Toast.LENGTH_LONG).show();
            }
            // Continue only if the file was successfully created
            if(photoFile != null){
                Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(cameraIntent, Utility.REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        timeView.setText(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date()));
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);

        //Save file path to use with ACTION_VIEW intents
        mPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Utility.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Picasso.get()
                    .load(mPhotoPath)
                    .into(imageView);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("photoPath", mPhotoPath);
    }
}
