package ru.azaychikov.ibstest.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import ru.azaychikov.ibstest.R;
import ru.azaychikov.ibstest.model.Image;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        try {
            MainActivity.ImageAdapter adapter = new MainActivity.ImageAdapter(this, Image.getSpacePhotos());
            recyclerView.setAdapter(adapter);
        } catch(NullPointerException ex) {
            Intent intent = new Intent(this, ServerErrorActivity.class);
            startActivity(intent);
        }

    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>  {

        @Override
        public ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the layout
            View photoView = inflater.inflate(R.layout.item_image, parent, false);

            ImageAdapter.MyViewHolder viewHolder = new ImageAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ImageAdapter.MyViewHolder holder, int position) {

            Image spacePhoto = mSpacePhotos[position];
            ImageView imageView = holder.mPhotoImageView;

            Glide.with(mContext)
                    .load(spacePhoto.getUrl())
                    .placeholder(R.drawable.ic_cloud_off_red)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return (mSpacePhotos.length);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ImageView mPhotoImageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    Image spacePhoto = mSpacePhotos[position];

                    Intent intent = new Intent(mContext, ImageActivity.class);
                    intent.putExtra(ImageActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    startActivity(intent);
                }
            }
        }

        private Image[] mSpacePhotos;
        private Context mContext;

        public ImageAdapter(Context context, Image[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;
        }
    }
}
