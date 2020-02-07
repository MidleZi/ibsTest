package ru.azaychikov.ibstest.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import ru.azaychikov.ibstest.R;
import ru.azaychikov.ibstest.model.Image;

/**
 * Экран отображения картинки
 */

public class ImageActivity extends Activity  {

    public static final String EXTRA_SPACE_PHOTO = "SpacePhotoActivity.SPACE_PHOTO";

    private PhotoView mImageView;
    private PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mImageView = (PhotoView) findViewById(R.id.image);
        Image spacePhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);
        photoViewAttacher = new PhotoViewAttacher(mImageView);
        photoViewAttacher.setZoomable(true);

        //Получаем картинку и отрисовываем на экране
        Glide.with(this)
                .load(spacePhoto.getUrl())
                .asBitmap()
                .error(R.drawable.ic_cloud_off_red)
                .listener(new RequestListener<String, Bitmap>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        onPalette(Palette.from(resource).generate());
                        mImageView.setImageBitmap(resource);

                        return false;
                    }

                    public void onPalette(Palette palette) {
                        if (null != palette) {
                            ViewGroup parent = (ViewGroup) mImageView.getParent().getParent();
                            parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
                        }
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImageView);

    }


}
