package ru.azaychikov.exampleretrofit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.azaychikov.exampleretrofit.R;
import ru.azaychikov.exampleretrofit.model.File;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    public static final String RESOURCE_ID = "ImageActivity.RESOURCE_ID";
    private PhotoView mImageView;
    private PhotoViewAttacher photoViewAttacher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageView = (PhotoView) findViewById(R.id.image);
        String fileUrl = getIntent().getExtras().getString(RESOURCE_ID);
        photoViewAttacher = new PhotoViewAttacher(mImageView);
        photoViewAttacher.setZoomable(true);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));

        builder.build().load(fileUrl)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(mImageView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
