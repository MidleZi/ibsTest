package ru.azaychikov.ibstest.data;

import java.util.ArrayList;

public class YaDiskFolder {
    private String public_key;
    private String public_url;
    private YaDiskEmbedded _embedded;

    public YaDiskEmbedded getEmb() {
        return _embedded;
    }

    public ArrayList<YaDiskFile> getFiles() {
        return getEmb().getItems();
    }
}
