package ru.azaychikov.ibstest.core;

import android.content.Context;

import java.util.List;
import java.util.Map;

import ru.azaychikov.ibstest.model.File;

public class Presenter implements GetDataContract.Presenter, GetDataContract.onGetDataListener {

    private GetDataContract.View mGetDataView;
    private Intractor mIntractor;
    public Presenter(GetDataContract.View mGetDataView){
        this.mGetDataView = mGetDataView;
        mIntractor = new Intractor(this);
    }

    @Override
    public void getDataFromURL(Context context, String url) {
        mIntractor.initRetrofitCallOnURL(context,url);
    }

    @Override
    public void getDataFromURL(Context context, String publicKey, String path) {
        mIntractor.initRetrofitCallWithPublicKeyAndPath(context,publicKey, path);
    }

    @Override
    public void onSuccess(String message, Map<String, List<File>> imageInFolders, String rootFolder) {
        mGetDataView.onGetDataSuccess(message, imageInFolders, rootFolder);
    }

    @Override
    public void onFailure(String message) {
        mGetDataView.onGetDataFailure(message);
    }
}
