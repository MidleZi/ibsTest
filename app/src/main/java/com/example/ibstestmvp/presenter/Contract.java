package com.example.ibstestmvp.presenter;

import com.example.ibstestmvp.entities.Item;
import java.util.List;
import java.util.Map;


//Контракт MVP
public interface Contract {
    interface View {
        void showImages(Map<String, List<Item>> data, String rootFolder);
        void showError();
    }

    interface Presenter {
        void getData();
        void getData(String publicKey, String path);
        void getDataFromDb();
        void getDataFolderFromDb(String folderName);
    }

    interface Repository {
        //Слушатель данных от модели
        interface OnRemoteListener {
            void onError(Throwable t);
            void onComplete(Map<String, List<Item>> items, String rootFolder);
        }

        void getData(OnRemoteListener onRemoteListener);
        void getData(OnRemoteListener onRemoteListener, String publicKey, String path);
        Map<String, List<Item>> getDataFromDb();
        String getRootFolderNameFromDb();
        Map<String, List<Item>> getDataFolderFromDb(String folderName);
    }
}
