package ru.azaychikov.ibstest.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import ru.azaychikov.ibstest.R;
import ru.azaychikov.ibstest.model.Image;
import ru.azaychikov.ibstest.model.YaDiskFile;
import ru.azaychikov.ibstest.model.YaDiskFolder;

/**
 * Главный экран
 */


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<YaDiskFile> files;
    private MainActivity.ImageAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String responseString;
    private String url;
    private JsonObjectRequest jsObjRequest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new MainActivity.ImageAdapter(this);
        queue = Volley.newRequestQueue(this);
        String publicUrl = "https://yadi.sk/d/G8AQKlUhT47Z_w";
        try {
            url = "https://cloud-api.yandex.net:443/v1/disk/public/resources?public_key=" + URLEncoder.encode(publicUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
            //Тут мы получаем данные от Я.диск
        jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            //В случаей успешного ответа, получаем JSON, парсим его в список какртинок и передаем в адаптер на отрисовку
            @Override
            public void onResponse(JSONObject response) {
                // TODO Auto-generated method stub
                responseString = response.toString();
                files = YaDiskFolder.responseToFolder(responseString).getImages();
                adapter.setmSpacePhotos(files);
            }
        }, new Response.ErrorListener() {

            //В случае ошибки ответа от сервера выводим сообщение об ошибке
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ошибка загрузки, попробуйте еще раз", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        //ставим выполнение запроса в очередь в volley
        queue.add(jsObjRequest);
        //отрисовываем лаяут главного экрана
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onRefresh() {
        recyclerView.setHasFixedSize(true);
        //ставим выполнение запроса в очередь в volley
        queue.add(jsObjRequest);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setRefreshing(false);
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

        private Image[] mSpacePhotos;
        private Context mContext;

        public ImageAdapter(Context context, Image[] spacePhotos) {
            mContext = context;
            mSpacePhotos = spacePhotos;
        }

        public ImageAdapter(Context context) {
            mContext = context;
            mSpacePhotos = new Image[0];
        }

        public void setmSpacePhotos(ArrayList<YaDiskFile> files){
            mSpacePhotos = Image.getSpacePhotos(files);
            notifyDataSetChanged();
        }

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
                    .load(spacePhoto.getUrlPreview())
                    .placeholder(R.drawable.ic_cloud_off_red)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
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
                if (position != RecyclerView.NO_POSITION) {
                    Image spacePhoto = mSpacePhotos[position];

                    Intent intent = new Intent(mContext, ImageActivity.class);
                    intent.putExtra(ImageActivity.EXTRA_SPACE_PHOTO, spacePhoto);
                    startActivity(intent);
                }
            }
        }
    }
}
