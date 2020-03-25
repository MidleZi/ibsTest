package ru.azaychikov.ibstest.core;

import android.content.Context;

import java.util.List;
import java.util.Map;

import ru.azaychikov.ibstest.model.File;

public interface GetDataContract {
    interface View{
        void onGetDataSuccess(String message, Map<String, List<File>> imageInFolders, String rootFolder);
        void onGetDataFailure(String message);
    }
    interface Presenter{
        void getDataFromURL(Context context, String url);
        void getDataFromURL(Context context, String publicKey, String path);
    }
    interface Interactor{
        void initRetrofitCallOnURL(Context context, String url);
        void initRetrofitCallWithPublicKeyAndPath(Context context, String publicKey, String path);

    }
    interface onGetDataListener{
        void onSuccess(String message, Map<String, List<File>> imageInFolders, String rootFolder);
        void onFailure(String message);
    }
}
