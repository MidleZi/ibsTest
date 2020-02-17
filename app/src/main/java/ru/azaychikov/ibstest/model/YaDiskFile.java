package ru.azaychikov.ibstest.model;

/**
 * Сущность для описания файла на Я.диске
 */

public class YaDiskFile {

    private String file;
    private String media_type;
    private String name;
    private String preview;
    private String resourceId;
    private String modified;

    public YaDiskFile(String file, String name, String media_type, String preview, String resourseId, String modified){
        this.file = file;
        this.name = name;
        this.media_type = media_type;
        this.preview = preview;
        this.resourceId = resourseId;
        this.modified = modified;
    }

    public String getFile() {
        return file;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getName() {
        return name;
    }

    public String getPreview() {
        return preview;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getModified() {
        return modified;
    }

    @Override
    public String toString() {
        return "YaDiskFile{" +
                "\nfile='" + file + '\'' +
                ",\n media_type='" + media_type + '\'' +
                ",\n name='" + name + '\'' +
                ",\n preview='" + preview + '\'' +
                ",\n resourceId='" + resourceId + '\'' +
                ",\n modified='" + modified + '\'' +
                '}';
    }
}
