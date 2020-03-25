package ru.azaychikov.ibstest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.azaychikov.ibstest.R;
import ru.azaychikov.ibstest.adapter.CustomAdapter;
import ru.azaychikov.ibstest.core.GetDataContract;
import ru.azaychikov.ibstest.core.Presenter;
import ru.azaychikov.ibstest.model.File;

import static ru.azaychikov.ibstest.model.File.getImageFromFolder;

public class FolderActivity extends AppCompatActivity implements GetDataContract.View, SwipeRefreshLayout.OnRefreshListener {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    private static Map<String, List<File>> imageInFolders = new HashMap<>();
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Presenter mPresenter;
    private String publicKey;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        final File folder;
        if(arguments!=null){
            folder = arguments.getParcelable(File.class.getSimpleName());
            publicKey = folder.getPublicKey();
            path = folder.getPath();
            getSupportActionBar().setSubtitle(folder.getName());
        }

        progressDoalog = new ProgressDialog(FolderActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        mPresenter = new Presenter(this);
        mPresenter.getDataFromURL(getApplicationContext(), publicKey, path);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.customRecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
    }

    public void generateDataList(List<File> fileList) {
        CustomAdapter.OnFileClickListener onFileClickListener = new CustomAdapter.OnFileClickListener() {
            @Override
            public void onFileClick(File file) {
                if(file.getType().equals("file")) {
                    Intent intent = new Intent(FolderActivity.this, ImageActivity.class);
                    intent.putExtra(File.class.getSimpleName(), file);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(FolderActivity.this, FolderActivity.class);
                    intent.putExtra(File.class.getSimpleName(), file);
                    startActivity(intent);
                }
            }
        };

        adapter = new CustomAdapter(this, fileList, onFileClickListener, imageInFolders);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        recyclerView.setHasFixedSize(true);
        mPresenter.getDataFromURL(getApplicationContext(), publicKey, path);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onGetDataSuccess(String message, Map<String, List<File>> imageInFolders, String rootFolder) {
        progressDoalog.dismiss();
        System.out.println(message);
        if(adapter == null) {
            System.out.println(imageInFolders.get(rootFolder));
            generateDataList(getImageFromFolder(imageInFolders.get(rootFolder)));
        } else {
            adapter.setImageInFolders(imageInFolders);
        }
    }

    @Override
    public void onGetDataFailure(String message) {
        progressDoalog.dismiss();
        Toast.makeText(FolderActivity.this, message, Toast.LENGTH_SHORT).show();
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
