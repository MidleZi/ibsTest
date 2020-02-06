package ru.azaychikov.ibstest.model;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Сущность для описания папки на Я.диске
 */

public class YaDiskFolder {

    private YaDiskEmbedded _embedded;
    private ArrayList<YaDiskFile> images;

    public static YaDiskFolder responseToFolder(String responseString) {
        Gson g = new Gson();
        YaDiskFolder folder = g.fromJson(responseString, YaDiskFolder.class);
        return folder;
    }

    public YaDiskEmbedded getEmb() {
        return _embedded;
    }

    public ArrayList<YaDiskFile> getFiles() {
        return getEmb().getItems();
    }

    public ArrayList<YaDiskFile> getImages() {
        images = new ArrayList<>();
        for (YaDiskFile file : getEmb().getItems()) {
            if(file.getMedia_type().equals("image")){
                images.add(file);
            }
        }
        return images;
    }
}
