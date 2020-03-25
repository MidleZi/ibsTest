package ru.azaychikov.ibstest.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ortiz.touchview.TouchImageView;

import ru.azaychikov.ibstest.R;
import ru.azaychikov.ibstest.model.File;

public class ImageActivity extends AppCompatActivity {

    public static final String RESOURCE_ID = "ImageActivity.RESOURCE_ID";
    private TouchImageView mImageView;
    //private PhotoViewAttacher photoViewAttacher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        File file = null;
        if(arguments!=null){
            file = arguments.getParcelable(File.class.getSimpleName());
            System.out.println(file.toString());
            getSupportActionBar().setTitle(file.getName());
        }

        mImageView = (TouchImageView) findViewById(R.id.image);
//        photoViewAttacher = new PhotoViewAttacher(mImageView);
//        photoViewAttacher.setZoomable(true);

        Glide.with(this)
                .load(file.getFile())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
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
