package ru.azaychikov.ibstest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import ru.azaychikov.ibstest.data.DataFromYaDisk;
import ru.azaychikov.ibstest.data.YaDiskFile;

public class Image implements Parcelable {

    private String mUrl;
    private String mTitle;

    public Image(String url, String title) {
        mUrl = url;
        mTitle = title;
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

    public static  Image[] getSpacePhotos() {
        ArrayList<YaDiskFile> files = DataFromYaDisk.getFilesFromYandexDiskFolder("https://yadi.sk/d/G8AQKlUhT47Z_w");
        //files.add(new YaDiskFile("https://downloader.disk.yandex.ru/disk/57c10cd7ec2d79a71aca4821cc32f053c1637014f6f07765e0dc21fa7661d7cf/5e3aaa50/bAivw64c2O-nAMw7W4M5XHLflyeQewUz0Pj2WrKlqVdCwkC4z9QdJAK6ClHjVJuX31SiHi666d_jPuLKRn2e7g%3D%3D?uid=0&filename=venus.png&disposition=attachment&hash=&limit=0&content_type=image%2Fpng&owner_uid=0&fsize=85688&hid=de0145a75ddf50367d5bc55818849908&media_type=image&tknv=v2&etag=f71de2a594c531813143df0ee2a7b387","venus.png", "image"));
        Image[] image = new Image[files.size()];

        for(int i = 0; i < files.size(); i++) {
            if(files.get(i).getMedia_type().equals("image")){
                image[i] = new Image(files.get(i).getFile(), files.get(i).getName());
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
