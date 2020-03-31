package ru.azaychikov.ibstestmvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.azaychikov.ibstestmvvm.R;
import ru.azaychikov.ibstestmvvm.adapter.CustomAdapter;
import ru.azaychikov.ibstestmvvm.model.File;
import ru.azaychikov.ibstestmvvm.model.Root;
import ru.azaychikov.ibstestmvvm.viewmodel.FileViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView my_recycler_view;
    private ProgressBar progress_circular_movie_article;
    private LinearLayoutManager layoutManager;
    private CustomAdapter adapter;
    private Map<String, List<File>> imageInFolders = new HashMap<>();
    private CustomAdapter.OnFileClickListener onFileClickListener;
    FileViewModel fileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
        System.out.println("После initialization(); в MainActivity");
        getAllFiles();
        System.out.println("После getAllFiles(); в MainActivity");
    }


    /**
     * initialization of views and others
     *
     * @param @null
     */
    private void initialization() {
       // progress_circular_movie_article = (ProgressBar) findViewById(R.id.progress_circular_movie_article);
        my_recycler_view = (RecyclerView) findViewById(R.id.customRecyclerView);

        // use a linear layout manager
        layoutManager = new GridLayoutManager(this, 2);
        my_recycler_view.setLayoutManager(layoutManager);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        my_recycler_view.setHasFixedSize(true);

        // adapter
        onFileClickListener = new CustomAdapter.OnFileClickListener() {
            @Override
            public void onFileClick(File file) {
                if(file.getType().equals("file")) {
                    Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                    intent.putExtra(File.class.getSimpleName(), file);
                    startActivity(intent);
                }
                //else {
//                    Intent intent = new Intent(MainActivity.this, FolderActivity.class);
//                    intent.putExtra(File.class.getSimpleName(), file);
//                    startActivity(intent);
//                }
            }
        };
        adapter = new CustomAdapter(MainActivity.this, imageInFolders, onFileClickListener);
        my_recycler_view.setAdapter(adapter);

        // View Model
        fileViewModel = ViewModelProviders.of(this).get(FileViewModel.class);
    }

    /**
     * get movies articles from news api
     *
     * @param @null
     */
    private void getAllFiles() {
        fileViewModel.getFilesLiveData().observe(this, new Observer<Map<String, List<File>>>() {
            @Override
            public void onChanged(Map<String, List<File>> files) {
                if (files != null) {
                    System.out.println("уведомляем адаптер об изенениях");
                    imageInFolders = files;
                    adapter.setImageInFolders(files);
                    adapter.notifyDataSetChanged();
//                    adapter = new CustomAdapter(MainActivity.this, imageInFolders, onFileClickListener);
//                    my_recycler_view.setAdapter(adapter);
                    System.out.println("Уведомили");
                }
            }
        });
    }
}
