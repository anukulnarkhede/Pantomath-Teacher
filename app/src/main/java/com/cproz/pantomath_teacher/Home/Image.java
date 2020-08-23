package com.cproz.pantomath_teacher.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.cproz.pantomath_teacher.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class Image extends AppCompatActivity {

    ZoomageView imageView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);




        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        imageView = findViewById(R.id.imageView9);

        Picasso.get().load(bundle.getString("Photo1")).into(imageView);



    }
}