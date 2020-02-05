package ru.azaychikov.ibstest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import ru.azaychikov.ibstest.data.DataFromYaDisk;
import ru.azaychikov.ibstest.data.GetableFiles;
import ru.azaychikov.ibstest.data.YaDiskFile;
import ru.azaychikov.ibstest.data.YaDiskFolder;

public class Image implements Parcelable {

    private String mUrl;
    private String mTitle;
    private static DataFromYaDisk disk;
    private static ArrayList<YaDiskFile> files;

    public Image(String url, String title) {
        mUrl = url;
        mTitle = title;
        disk.setGetableFiles(new GetableFiles() {
            @Override
            public void onResponse(String responseText) {
                getSpacePhotos(responseText);
            }
        });
    }

    protected Image(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static Image[] getSpacePhotos(String responseText) {
        disk = new DataFromYaDisk();
        System.out.println(responseText);
        YaDiskFolder folder = disk.responseToFolder(responseText);
        files = folder.getFiles();

        Image[] image = new Image[files.size()];
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getMedia_type().equals("image")) {
                image[i] = new Image(files.get(i).getFile(), files.get(i).getName());
                System.out.println(i);
                System.out.println(image[i].mTitle + " " + "\nЗагружен!");
            }
        }
        System.out.println(image.length);
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}
