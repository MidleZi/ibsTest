package ru.azaychikov.exampleretrofit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.azaychikov.exampleretrofit.R;
import ru.azaychikov.exampleretrofit.adapter.CustomAdapter;
import ru.azaychikov.exampleretrofit.model.File;
import ru.azaychikov.exampleretrofit.model.Root;
import ru.azaychikov.exampleretrofit.network.GetDataService;
import ru.azaychikov.exampleretrofit.network.RetrofitClientInstance;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static ru.azaychikov.exampleretrofit.model.File.getImageFromFolder;

public class FolderActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    private static Map<String, List<File>> imageInFolders = new HashMap<>();
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GetDataService service;
    private String rootFolder;
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

        /*Create handle for the RetrofitInstance interface*/
        service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        firstLevelGet();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
    }

    public void generateDataList(List<File> fileList) {
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.customRecyclerView);

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
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        recyclerView.setHasFixedSize(true);
        firstLevelGet();
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void firstLevelGet() {
        Call<Root> call = service.getAll(publicKey, path);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                progressDoalog.dismiss();
                if (response.code() == 200) {
                    imageInFolders.put(response.body().getName(), response.body().getEmbedded().getItems());
                    rootFolder = response.body().getName();
                    for (File file : File.getFolderFromFileList(response.body().getEmbedded().getItems())) {
                        if(file.getType().equals("dir")) {
                            secondLevelGet(file);
                        }
                    }
                    generateDataList(getImageFromFolder(imageInFolders.get(rootFolder)));
                } else if(response.code() == 404) {
                    Toast.makeText(FolderActivity.this, "Файл не найден", Toast.LENGTH_SHORT).show();
                } else if(response.code() == 503) {
                    Toast.makeText(FolderActivity.this, "Проблемы с сервером", Toast.LENGTH_SHORT).show();
                } else {
                    firstLevelGet();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(FolderActivity.this, "Проблемы с интернет соедининем, проверьте подключнние к интернету", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void secondLevelGet(final File file) {
        Call<Root> call = service.getAll(file.getPublicKey(),file.getPath());

        call.enqueue(new Callback<Root>() {

            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.code() == 200) {
                    imageInFolders.put(response.body().getName(), response.body().getEmbedded().getItems());
                    generateDataList(getImageFromFolder(imageInFolders.get(rootFolder)));
                } else if(response.code() == 404) {
                    Toast.makeText(FolderActivity.this, "Файл не найден", Toast.LENGTH_SHORT).show();
                } else if(response.code() == 503) {
                    Toast.makeText(FolderActivity.this, "Проблемы с сервером", Toast.LENGTH_SHORT).show();
                } else {
                    secondLevelGet(file);
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(FolderActivity.this, "Проблемы с интернет соедининем, проверьте подключнние к интернету", Toast.LENGTH_SHORT).show();
            }
        });
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
