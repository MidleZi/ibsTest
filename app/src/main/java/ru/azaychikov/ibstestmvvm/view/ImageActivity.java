package ru.azaychikov.ibstestmvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.azaychikov.ibstestmvvm.R;
import ru.azaychikov.ibstestmvvm.model.File;

import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ortiz.touchview.TouchImageView;

public class ImageActivity extends AppCompatActivity {

    private TouchImageView mImageView;

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
            getSupportActionBar().setTitle(file.getName());
        }

        mImageView = (TouchImageView) findViewById(R.id.image);

        System.out.println(file);

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
