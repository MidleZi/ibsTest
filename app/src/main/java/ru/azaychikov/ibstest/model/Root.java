package ru.azaychikov.ibstest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Root {

    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("public_url")
    @Expose
    private String publicUrl;
    @SerializedName("_embedded")
    @Expose
    private Embedded embedded;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("resource_id")
    @Expose
    private String resourceId;
    @SerializedName("revision")
    @Expose
    private Long revision;
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
    @SerializedName("views_count")
    @Expose
    private Integer viewsCount;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
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

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
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

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }
}
