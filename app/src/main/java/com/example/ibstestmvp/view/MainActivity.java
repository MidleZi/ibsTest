package com.example.ibstestmvp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.ibstestmvp.R;
import com.example.ibstestmvp.adapter.Adapter;
import com.example.ibstestmvp.entities.Item;
import com.example.ibstestmvp.presenter.Contract;
import com.example.ibstestmvp.presenter.Presenter;
import com.example.ibstestmvp.room.DbHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Contract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    private Contract.Presenter mPresenter;
    private RecyclerView recyclerView;
    private Map<String, List<Item>> items = new HashMap<>();
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPresenter = new Presenter(this);
        recyclerView = findViewById(R.id.list);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //Запрашиваем у презентера данные для отображения
        //локальные для загрузки списка элементов
        mPresenter.getDataFromDb();
        //с сервера для обновления списка
        mPresenter.getData();
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
    public void onRefresh() {
        recyclerView.setHasFixedSize(true);
        mPresenter.getData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {
        Toast.makeText(getApplicationContext(), "Что-то пошло не так, проверьте подключение к интернету", Toast.LENGTH_LONG).show();
    }

}
