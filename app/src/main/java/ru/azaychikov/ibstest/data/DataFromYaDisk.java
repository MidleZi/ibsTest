package ru.azaychikov.ibstest.data;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataFromYaDisk {

    private String responseString;
    private int responseCode;
    private YaDiskFolder folder;
    private ArrayList<YaDiskFile> files;
    private GetableFiles getableFiles;

    public GetableFiles getGetableFiles() {
        return getableFiles;
    }

    public void setGetableFiles(GetableFiles getableFiles){
        this.getableFiles = getableFiles;
    }

    public YaDiskFolder responseToFolder(String responseString) {
        Gson g = new Gson();
        YaDiskFolder folder = g.fromJson(responseString, YaDiskFolder.class);
        return folder;
    }

    public ArrayList<YaDiskFile> getFilesFromYandexDiskFolder(String url) {
        String answ = null;
        try {
            answ = url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<YaDiskFile>();
    }

    public void sendGet(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    getableFiles.onResponse(response.body().string());
                }
            }
        });
    }

    public ArrayList<YaDiskFile> getFiles(){
        return files;
    }

    public static String urlEncodeToYaDisk(String parameter) {
        String url = null;
        try {
            url = "https://cloud-api.yandex.net:443/v1/disk/public/resources?public_key=" + URLEncoder.encode(parameter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


}
