package ru.azaychikov.ibstest.core;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.azaychikov.ibstest.model.File;
import ru.azaychikov.ibstest.model.Root;
import ru.azaychikov.ibstest.network.GetDataService;
import ru.azaychikov.ibstest.network.RetrofitClientInstance;

public class Intractor implements GetDataContract.Interactor {

    private GetDataContract.onGetDataListener mOnGetDatalistener;
    private Map<String, List<File>> imageInFolders = new HashMap<>();
    private String rootFolder;
    private String folder;

    public Intractor(GetDataContract.onGetDataListener mOnGetDatalistener){
        this.mOnGetDatalistener = mOnGetDatalistener;
    }

    @Override
    public void initRetrofitCallOnURL(final Context context, final String url) {
        GetDataService request = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Root> call = request.getAll(url);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.code() == 200) {
                    imageInFolders.put(response.body().getName(), response.body().getEmbedded().getItems());
                    rootFolder = response.body().getName();
                    for (File file : File.getFolderFromFileList(response.body().getEmbedded().getItems())) {
                        if(file.getType().equals("dir")) {
                            initRetrofitCallWithPublicKeyAndPath(context, file.getPublicKey(), file.getPath());
                        }
                    }
                    mOnGetDatalistener.onSuccess(rootFolder + " size: " + imageInFolders.get(rootFolder).size(), imageInFolders, rootFolder);
                } else if(response.code() == 404) {
                    Toast.makeText(context, "Файл не найден", Toast.LENGTH_SHORT).show();
                } else if(response.code() == 503) {
                    Toast.makeText(context, "Проблемы с сервером", Toast.LENGTH_SHORT).show();
                } else {
                    initRetrofitCallOnURL(context, url);
                }
            }
            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.v("Error", t.getMessage());
                mOnGetDatalistener.onFailure("Проблемы с интернетом, проверьте интернет подключение");
            }
        });
    }

    @Override
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
                    mOnGetDatalistener.onSuccess(folder + " size: " + imageInFolders.get(folder).size(), imageInFolders, folder);
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
                mOnGetDatalistener.onFailure("Проблемы с интернетом, проверьте интернет подключение");
            }
        });
    }
}
