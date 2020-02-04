package ru.azaychikov.ibstest.data;

import java.util.ArrayList;

public class YaDiskEmbedded {

    private ArrayList<YaDiskFile> items;

    public ArrayList<YaDiskFile> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Embedded{" +
                "items=" + items +
                '}';
    }
}
