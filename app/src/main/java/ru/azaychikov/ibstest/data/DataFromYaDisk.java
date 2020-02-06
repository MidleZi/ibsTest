package ru.azaychikov.ibstest.data;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DataFromYaDisk {

    private ArrayList<YaDiskFile> files;

    public static YaDiskFolder responseToFolder(String responseString) {
        Gson g = new Gson();
        YaDiskFolder folder = g.fromJson(responseString, YaDiskFolder.class);
        return folder;
    }
}
