package com.example.ibstestmvp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.ibstestmvp.R;
import com.example.ibstestmvp.adapter.Adapter;
import com.example.ibstestmvp.entities.Item;
import com.example.ibstestmvp.presenter.Contract;
import com.example.ibstestmvp.presenter.Presenter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderActivity extends AppCompatActivity implements Contract.View, SwipeRefreshLayout.OnRefreshListener  {

    private static final String TAG = "FolderActivity";

    private Contract.Presenter mPresenter;
    private RecyclerView recyclerView;
    private Map<String, List<Item>> items = new HashMap<>();
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String publicKey;
    private String path;
    private String folderName;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        final Item folder;
        if(arguments!=null){
            folder = arguments.getParcelable(Item.class.getSimpleName());
            publicKey = folder.getPublicKey();
            path = folder.getPath();
            folderName = folder.getName();
            getSupportActionBar().setSubtitle(folder.getName());
        }

        mPresenter = new Presenter(this);
        recyclerView = findViewById(R.id.list);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //Запрашиваем у презентера данные для отображения
        //локальные для загрузки списка элементов
        mPresenter.getDataFolderFromDb(folderName);
        //с сервера для обновления списка
        mPresenter.getData(publicKey, path);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getData();
            }
        });
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public void showImages(Map<String, List<Item>> data, String folder) {
        items = data;
        if(data != null) {
            if (adapter == null) {
                adapter = new Adapter(this, data.get(folder));
                recyclerView.setAdapter(adapter);
            }
            adapter.loadedNewItems(data);
        }
        Log.d(TAG, "showMessage()");
    }

    @Override
    public void showError(){
        Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRefresh() {
        recyclerView.setHasFixedSize(true);
        mPresenter.getData();
        swipeRefreshLayout.setRefreshing(false);
    }
}
