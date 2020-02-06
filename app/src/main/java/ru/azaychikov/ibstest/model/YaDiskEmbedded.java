package ru.azaychikov.ibstest.model;

import java.util.ArrayList;

/**
 * Сущность для описания чего-то промежуточного между папкой и файлом (: на Я.диске (нужно для парсинга JSON)
 */

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
