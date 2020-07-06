package com.example.ibstestmvp.room;

import android.os.AsyncTask;
import android.util.Log;
import com.example.ibstestmvp.App;
import com.example.ibstestmvp.entities.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DbHelper {

    //Врапер над методами доступа к бд (асинхронное выполнение)
    private static final String TAG = "DbHelper";
    private AppDatabase db = App.getInstance().getDatabase();

    public List<Item> getAll() {
        List<Item> items = null;
        try {
            items = new DbHelperExecutorGetAllItem().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> getAllInFolder(String folder) {
        List<Item> items = null;
        try {
            items = new DbHelperExecutorGetAllItemInFolder(folder).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<String> getFoldersName(){
        List<String> foldersName = new ArrayList<>();
        try {
            foldersName = new DbHelperExecutorGetFoldersName().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return foldersName;
    }

    public String getRootFolderName() {
        String rootFolderName = null;
        Item item= null;
        try {
            item = new DbHelperExecutorGetRootFolderName().execute().get();
            if(item != null) {
                rootFolderName = item.getName();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rootFolderName;
    }

    public Item getById(long id) {
        Item item = null;
        try {
            item = new DbHelperExecutorGetItemById(id).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return item;
    }

    public void insertItem(Item item) {
        new DbHelperExecutorInsertItem(item).execute();
        Log.d(TAG, item.getName() + " inserted");
    }

    public void update(Item item) {
        new DbHelperExecutorUpdateItem(item).execute();
        Log.d(TAG, item.getName() + " updated");
    }

    public void deleteAllItems() {
        new DbHelperExecutorDeleteAllItem().execute();
        Log.d(TAG, "All items delted");
    }

    public Map<String, List<Item>> getDataFromDb() {
        Map<String, List<Item>> dbItems = new HashMap<>();
        List<String> folderNames = getFoldersName();
        for(String folder : folderNames){
            dbItems.put(folder, getAllInFolder(folder));
        }
        if(dbItems.size() == 0) {
            return null;
        }
        return dbItems;
    }

    private class DbHelperExecutorGetItemById extends AsyncTask<Void, Void, Item> {
        private long id;

        DbHelperExecutorGetItemById(long id) {
            this.id = id;
        }

        @Override
        protected Item doInBackground(Void... voids) {
            return db.getItemDao().getById(id);
        }
    }

    private class DbHelperExecutorGetAllItem extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... voids) {
            return db.getItemDao().getAll();
        }
    }

    private class DbHelperExecutorGetAllItemInFolder extends AsyncTask<Void, Void, List<Item>> {
        private String folder;

        DbHelperExecutorGetAllItemInFolder(String folder) {
            this.folder = folder;
        }

        @Override
        protected List<Item> doInBackground(Void... voids) {
            return db.getItemDao().getAllInFolder(folder);
        }
    }

    private class DbHelperExecutorGetFoldersName extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            return db.getItemDao().getFoldersName();
        }
    }

    private class DbHelperExecutorGetRootFolderName extends AsyncTask<Void, Void, Item> {
        @Override
        protected Item doInBackground(Void... voids) {
            return db.getItemDao().getRootFolderName();
        }
    }

    private class DbHelperExecutorInsertItem extends AsyncTask<Void, Void, Void> {
        private Item item;

        DbHelperExecutorInsertItem(Item item) {
            this.item = item;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.getItemDao().insert(item);
            return null;
        }
    }

    private class DbHelperExecutorUpdateItem extends AsyncTask<Void, Void, Void> {
        private Item item;

        DbHelperExecutorUpdateItem(Item item) {
            this.item = item;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.getItemDao().update(item);
            return null;
        }
    }

    private class DbHelperExecutorDeleteAllItem extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            db.getItemDao().deletdAllItem();
            return null;
        }
    }










}

