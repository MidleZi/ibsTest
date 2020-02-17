package ru.azaychikov.ibstest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Сущность для описания картинки
 */

public class Image implements Parcelable {

    private String mUrl;
    private String mUrlPreview;
    private String mTitle;

    public Image(String url, String title, String preview) {
        mUrl = url;
        mTitle = title;
        mUrlPreview = preview;
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

    public String getUrlPreview() {
        return mUrlPreview;
    }

    public String getName() {
        return mUrl;
    }

    public static Image[] getSpacePhotos(ArrayList<YaDiskFile> files) {
        Image[] image = new Image[files.size()];
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getMedia_type().equals("image")) {
                image[i] = new Image(files.get(i).getFile(), files.get(i).getName(),files.get(i).getPreview());
            }
        }
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
