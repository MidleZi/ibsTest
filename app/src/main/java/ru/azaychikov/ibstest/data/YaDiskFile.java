package ru.azaychikov.ibstest.data;

public class YaDiskFile {

    private String mime_type;
    private String file;
    private String media_type;
    private String name;

    public YaDiskFile(String file, String name, String media_type){
        this.file = file;
        this.name = name;
        this.media_type = media_type;
    }

    public String getMime_type() {
        return mime_type;
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
}
