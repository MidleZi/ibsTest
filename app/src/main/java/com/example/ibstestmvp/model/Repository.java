package com.example.ibstestmvp.model;

import android.util.Log;
import com.example.ibstestmvp.entities.Item;
import com.example.ibstestmvp.entities.RootFolder;
import com.example.ibstestmvp.presenter.Contract;
import com.example.ibstestmvp.network.GetDataService;
import com.example.ibstestmvp.network.RetrofitClientInstance;
import com.example.ibstestmvp.room.DbHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Repository implements Contract.Repository {

    private Map<String, List<Item>> data = new HashMap<>();
    private String url = "https://yadi.sk/d/G8AQKlUhT47Z_w";
    private String rootFolderName;
    private static final String TAG = "Repository";
    private OnRemoteListener onRemoteListener;
    private DbHelper db = new DbHelper();
    private List<Item> iemsForRewriteDb = new ArrayList<>();

    @Override
    public void getData(OnRemoteListener onRemoteListener) {
        this.onRemoteListener = onRemoteListener;
        getObservableRoot().subscribeWith(getObserver());
    }

    //Получаем данные катлога с сервера, зная к нем путь
    @Override
    public void getData(OnRemoteListener onRemoteListener, String publicKey, String path) {
        this.onRemoteListener = onRemoteListener;
        getObservableFolder(publicKey, path).subscribeWith(getObserver());
    }

    //Создаем наблюдаемый поток для получения содержимого корневого каталога
    public Observable<RootFolder> getObservableRoot() {
        return RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class)
                .getAll(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //Создаем наблюдаемый поток для получения содержимого каталога, згая к нему путь
    public Observable<RootFolder> getObservableFolder(String publicKey, String path) {
        return RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class)
                .getAll(publicKey, path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    //Создаем слушателя наблюдаемых потоков получения данных с сервера
    public DisposableObserver<RootFolder> getObserver() {
        return new DisposableObserver<RootFolder>() {
            //Знаю, это не самое лучшеt решtние, но оно работает. FlatMap еще не разобрал
            @Override
            public void onNext(RootFolder rootFolder) {
                rootFolderName = rootFolder.getName();
                List<Item> itemsWithFolder = new ArrayList<>();
                for (Item item : rootFolder.getEmbedded().getItems()) {
                    item.setFolder(rootFolderName);
                    itemsWithFolder.add(item);
                    iemsForRewriteDb.add(item);
                }
                if (rootFolder.getPath().equals("/")) {
                    iemsForRewriteDb.add((new Item(rootFolder.getPath(), rootFolderName, rootFolder.getType())));
                }
                data.put(rootFolder.getName(), itemsWithFolder);

                for (Item item : Item.getFolderFromItemList(rootFolder.getEmbedded().getItems())) {
                    if (item.getType().equals("dir")) {
                        getObservableFolder(item.getPublicKey(), item.getPath()).subscribeWith(getObserver());
                    }
                }
                rewriteDataOnDb(iemsForRewriteDb);
                Log.d(TAG, "OnNext " + rootFolder.getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error" + e);
                onRemoteListener.onError(e);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                onRemoteListener.onComplete(data, rootFolderName);
                Log.d(TAG, "Completed");
            }
        };
    }

    @Override
    public Map<String, List<Item>> getDataFromDb() {
        return db.getDataFromDb();
    }

    @Override
    public String getRootFolderNameFromDb() {
        return db.getRootFolderName();
    }

    @Override
    public Map<String, List<Item>> getDataFolderFromDb(String folderName) {
        Map<String, List<Item>> folderItems = new HashMap<>();
        List<Item> items = db.getAllInFolder(folderName);
        folderItems.put(folderName, items);
        if (folderItems.size() == 0) {
            return null;
        }
        for (Item item : items) {
            if (item.getType().equals("dir")) {
                folderItems.put(item.getName(), db.getAllInFolder(item.getName()));
            }
        }
        return folderItems;
    }

    //Проверяем, все ли в ответе есть в бд, если нету, то вставлем
    public void rewriteDataOnDb(List<Item> remoteItems) {
        List<Item> itemInFolder = db.getAll();
        for(Item item : remoteItems) {
            if (!itemInFolder.contains(item)) {
                db.insertItem(item);
            }
        }
    }
}
