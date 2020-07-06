package com.example.ibstestmvp.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Item implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("resource_id")
    @Expose
    private String resourceId;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("revision")
    @Expose
    private Long revision;
    @SerializedName("antivirus_status")
    @Expose
    private String antivirusStatus;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("mime_type")
    @Expose
    private String mimeType;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("preview")
    @Expose
    private String preview;
    @SerializedName("sha256")
    @Expose
    private String sha256;
    @SerializedName("md5")
    @Expose
    private String md5;

    private String folder;

    /**Метод возвращает из папки только изображения
     *
     * @param folder список файлов в папке
     * @return список изображений в папке
     */
    public static List<Item> getImageFromFolder(List<Item> folder) {
        List<Item> folderAndImage = new ArrayList<>();

        for (Item file : folder) {
            if (file.getMediaType() != null && file.getMediaType().equals("image") || file.getType().equals("dir")) {
                folderAndImage.add(file);
            }
        }
        return folderAndImage;
    }

    /**
     * Метод возвращает все папки в каталоге
     * @param items список всех элементов в каталоге
     * @return список папок в каталоге
     */
    public static List<Item> getFolderFromItemList(List<Item> items) {
        List<Item> folders = new ArrayList<>();
        for (Item item : items) {
            if (item.getType().equals("dir")) {
                folders.add(item);
            }
        }
        return folders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public String getAntivirusStatus() {
        return antivirusStatus;
    }

    public void setAntivirusStatus(String antivirusStatus) {
        this.antivirusStatus = antivirusStatus;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDownloadLink() {
        return file;
    }

    public void setItem(String file) {
        this.file = file;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }


    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return "Item{\n" +
                "public_key='" + getPublicKey() + '\'' + "\n" +
                ", name='" + getName() + '\'' + "\n" +
                ", type='" + getType() + '\'' + "\n" +
                ", path='" + getPath() + '\'' + "\n" +
                ", media_type='" + getMediaType() + '\'' + "\n" +
                // ", preview='" + preview + '\'' + "\n" +
                //  ", file='" + file + '\'' + "\n" +
                //", folderName='" + folderName + '\'' + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {


        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return
                Objects.equals(name, item.name) &&
                Objects.equals(resourceId, item.resourceId) &&
                Objects.equals(created, item.created) &&
                Objects.equals(modified, item.modified) &&
                Objects.equals(path, item.path) &&
                Objects.equals(type, item.type) &&
                Objects.equals(revision, item.revision) &&
                Objects.equals(size, item.size) &&
                Objects.equals(md5, item.md5) &&
                Objects.equals(folder, item.folder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resourceId, created, modified, path, type, revision, size, md5, folder);
    }

    public Item(int id, String publicKey, String name, String resourceId, String created, String modified, String path, String type, Long revision, String antivirusStatus, Integer size, String mimeType, String file, String mediaType, String preview, String sha256, String md5, String folder) {
        this.id = id;
        this.publicKey = publicKey;
        this.name = name;
        this.resourceId = resourceId;
        this.created = created;
        this.modified = modified;
        this.path = path;
        this.type = type;
        this.revision = revision;
        this.antivirusStatus = antivirusStatus;
        this.size = size;
        this.mimeType = mimeType;
        this.file = file;
        this.mediaType = mediaType;
        this.preview = preview;
        this.sha256 = sha256;
        this.md5 = md5;
        this.folder = folder;
    }

    @Ignore
    public Item(String path, String name, String type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    @Ignore
    public Item(String publicKey, String path, String name, String file) {
        this.publicKey = publicKey;
        this.path = path;
        this.name = name;
        this.file = file;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            String publicKey = source.readString();
            String path = source.readString();
            String name = source.readString();
            String file = source.readString();
            return new Item(publicKey, path, name, file);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(publicKey);
        parcel.writeString(path);
        parcel.writeString(name);
        parcel.writeString(file);
    }
}
