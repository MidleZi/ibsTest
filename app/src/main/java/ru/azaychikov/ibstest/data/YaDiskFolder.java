package ru.azaychikov.ibstest.data;

import java.util.ArrayList;

public class YaDiskFolder {

    private YaDiskEmbedded _embedded;
    private ArrayList<YaDiskFile> images;

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
