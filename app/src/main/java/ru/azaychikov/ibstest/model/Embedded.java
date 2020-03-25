package ru.azaychikov.ibstest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Embedded {

    @SerializedName("sort")
    @Expose
    private String sort;
    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("items")
    @Expose
    private List<File> items = null;
    @SerializedName("limit")
    @Expose
    private Integer limit;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("total")
    @Expose
    private Integer total;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public List<File> getItems() {
        return items;
    }

    public void setItems(List<File> items) {
        this.items = items;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Embedded{" +
                "sort='" + sort + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", items=" + items +
                ", limit=" + limit +
                ", offset=" + offset +
                ", path='" + path + '\'' +
                ", total=" + total +
                '}';
    }
}