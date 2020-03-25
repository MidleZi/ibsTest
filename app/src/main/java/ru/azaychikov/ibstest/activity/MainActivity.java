package ru.azaychikov.ibstest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.azaychikov.ibstest.R;
import ru.azaychikov.ibstest.adapter.CustomAdapter;
import ru.azaychikov.ibstest.model.File;
import ru.azaychikov.ibstest.model.Root;
import ru.azaychikov.ibstest.network.GetDataService;
import ru.azaychikov.ibstest.network.RetrofitClientInstance;
import static ru.azaychikov.ibstest.model.File.getImageFromFolder;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    private static String publicKey = "https://yadi.sk/d/G8AQKlUhT47Z_w";
    private static Map<String, List<File>> imageInFolders = new HashMap<>();
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GetDataService service;
    private String rootFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDoalog = new ProgressDialog(MainActivity.this);
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
                    Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                    intent.putExtra(File.class.getSimpleName(), file);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, FolderActivity.class);
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
        Call<Root> call = service.getAll(publicKey);
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
                } else if(response.code() == 404) {
                    Toast.makeText(MainActivity.this, "Файл не найден", Toast.LENGTH_SHORT).show();
                } else if(response.code() == 503) {
                    Toast.makeText(MainActivity.this, "Проблемы с сервером", Toast.LENGTH_SHORT).show();
                } else {
                    firstLevelGet();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MainActivity.this, "Проблемы с интернет соедининем, проверьте подключнние к интернету", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Файл не найден", Toast.LENGTH_SHORT).show();
                } else if(response.code() == 503) {
                    Toast.makeText(MainActivity.this, "Проблемы с сервером", Toast.LENGTH_SHORT).show();
                } else {
                    secondLevelGet(file);
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MainActivity.this, "Проблемы с интернет соедининем, проверьте подключнние к интернету", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
