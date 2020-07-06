package com.example.ibstestmvp.presenter;

import android.util.Log;
import com.example.ibstestmvp.entities.Item;
import com.example.ibstestmvp.model.Repository;
import java.util.List;
import java.util.Map;

public class Presenter implements Contract.Presenter, Contract.Repository.OnRemoteListener {
    private static final String TAG = "Presenter";

    private Contract.View mView;
    private Contract.Repository mRepository;
    public Presenter(Contract.View mView) {
        this.mView = mView;
        this.mRepository = new Repository();
        Log.d(TAG, "Constructor");
    }

    //запрашиваем данные корневого уровня у модели из бд
    @Override
    public void getDataFromDb(){
        mView.showImages(mRepository.getDataFromDb(), mRepository.getRootFolderNameFromDb());

    }

    //запрашиваем данные у модели, зная путь к папке из бд
    @Override
    public void getDataFolderFromDb(String folderName) {

        mView.showImages(mRepository.getDataFolderFromDb(folderName), folderName);
    }

    //запрашиваем данные корневого уровня у модели с сервера
    @Override
    public void getData() {
        mRepository.getData(this);
        Log.d(TAG, "Presenter getData()");
    }

    //запрашиваем данные у модели, зная путь к папке с сервера
    @Override
    public void getData(String publicKey, String path) {
        mRepository.getData(this, publicKey, path);
        Log.d(TAG, "onButtonWasClicked()");
    }

    //Уведомляем вью об успешном получени данных и передаем их
    @Override
    public void onComplete(Map<String, List<Item>> items, String rootFolder) {
        mView.showImages(items, rootFolder);
    }

    //Уведомляем вью об ошибке
    @Override
    public void onError(Throwable t) {
        mView.showError();
    }


}
