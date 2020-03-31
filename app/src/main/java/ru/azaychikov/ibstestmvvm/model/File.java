package ru.azaychikov.ibstestmvvm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class File implements Parcelable {

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

    public static List<File> getImageFromFolder(List<File> folder) {
        List<File> folderAndImage = new ArrayList<>();

        for(File file : folder) {
            if(file.getMediaType() != null && file.getMediaType().equals("image") || file.getType().equals("dir")){
                folderAndImage.add(file);
            }
        }
        return folderAndImage;
    }

    public static List<File> getFolderFromFileList(List<File> files) {
        List<File> folders = new ArrayList<>();
        for(File file : files) {
            if(file.getType().equals("dir")){
                folders.add(file);
            }
        }
        return folders;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
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

    @Override
    public String toString() {
        return "File{\n" +
                "public_key='" + getPublicKey() + '\'' + "\n" +
                ", name='" + getName() + '\'' + "\n" +
                ", type='" +  getType() + '\'' + "\n" +
                ", path='" + getPath() + '\'' + "\n" +
                ", media_type='" + getMediaType() + '\'' + "\n" +
               // ", preview='" + preview + '\'' + "\n" +
                ", file='" + file + '\'' + "\n" +
                //", folderName='" + folderName + '\'' + "\n" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file1 = (File) o;
        return Objects.equals(getName(), file1.getName()) &&
                Objects.equals(getPath(), file1.getPath()) &&
                Objects.equals(getMediaType(), file1.getMediaType()) &&
                Objects.equals(getPreview(), file1.getPreview()) &&
                Objects.equals(getFile(), file1.getFile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(),getPath(),getMediaType(), getPreview(), getFile());
    }

    public File(String publicKey, String path, String name, String file) {
        this.publicKey = publicKey;
        this.path = path;
        this.name = name;
        this.file = file;
    }

    public static final Creator<File> CREATOR = new Creator<File>() {
        @Override
        public File createFromParcel(Parcel source) {
            String publicKey = source.readString();
            String path = source.readString();
            String name = source.readString();
            String file = source.readString();
            return new File(publicKey, path,name, file);
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
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
