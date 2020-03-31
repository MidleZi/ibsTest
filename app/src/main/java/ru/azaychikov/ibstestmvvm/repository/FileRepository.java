package ru.azaychikov.ibstestmvvm.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.azaychikov.ibstestmvvm.model.File;
import ru.azaychikov.ibstestmvvm.model.Root;
import ru.azaychikov.ibstestmvvm.network.GetDataService;
import ru.azaychikov.ibstestmvvm.network.RetrofitClientInstance;

public class FileRepository  {

    private static final String TAG = FileRepository.class.getSimpleName();
    private GetDataService apiRequest;
    private Map<String, List<File>> imageInFolders = new HashMap<>();
    private String folder;

    public FileRepository() {
        apiRequest = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
    }

    public LiveData<Map<String, List<File>>> getAllFiles(final Context context, final String publicKey) {
        final MutableLiveData<Map<String, List<File>>> data = new MutableLiveData<>();
        apiRequest.getAll(publicKey)
                .enqueue(new Callback<Root>() {

                    @Override
                    public void onResponse(Call<Root> call, Response<Root> response) {
                        Log.d(TAG, "onResponse response:: " + response);

                        if (response.code() == 200) {
                            imageInFolders.put(response.body().getName(), response.body().getEmbedded().getItems());
                            folder = response.body().getName();
                            for (File file : File.getFolderFromFileList(response.body().getEmbedded().getItems())) {
                                if(file.getType().equals("dir")) {
                                    initRetrofitCallWithPublicKeyAndPath(context, file.getPublicKey(), file.getPath());
                                }
                            }
                            System.out.println(imageInFolders.size());
                            data.setValue(imageInFolders);
                        } else if(response.code() == 404) {
                            Toast.makeText(context, "Файл не найден", Toast.LENGTH_SHORT).show();
                        } else if(response.code() == 503) {
                            Toast.makeText(context, "Проблемы с сервером", Toast.LENGTH_SHORT).show();
                        } else {
                            getAllFiles(context, publicKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<Root> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public void initRetrofitCallWithPublicKeyAndPath(final Context context, final String publicKey, final String path) {
        GetDataService request = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Root> call = request.getAll(publicKey, path);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.code() == 200) {
                    imageInFolders.put(response.body().getName(), response.body().getEmbedded().getItems());
                    response.body();
                    folder = response.body().getName();
                    for (File file : File.getFolderFromFileList(response.body().getEmbedded().getItems())) {
                        if(file.getType().equals("dir")) {
                            initRetrofitCallWithPublicKeyAndPath(context, file.getPublicKey(), file.getPath());
                        }
                    }
                    System.out.println("Второй уровень: " + imageInFolders.size());
                } else if(response.code() == 404) {
                    Toast.makeText(context, "Файл не найден", Toast.LENGTH_SHORT).show();
                } else if(response.code() == 503) {
                    Toast.makeText(context, "Проблемы с сервером", Toast.LENGTH_SHORT).show();
                } else {
                    initRetrofitCallWithPublicKeyAndPath(context, publicKey, path);
                }
            }
            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.v("Error",t.getMessage());
                //mOnGetDatalistener.onFailure("Проблемы с интернетом, проверьте интернет подключение");
            }
        });
    }
}
