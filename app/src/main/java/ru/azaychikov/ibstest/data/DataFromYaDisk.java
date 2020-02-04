package ru.azaychikov.ibstest.data;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataFromYaDisk  {

    public static ArrayList<YaDiskFile> getFilesFromYandexDiskFolder(String url) {
        String answ = null;
        try {
            answ = sendGet(urlEncodeToYaDisk(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson g = new Gson();
        YaDiskFolder folder = g.fromJson(answ, YaDiskFolder.class);
                return folder.getFiles();
    }

    private static String sendGet(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response   response = call.execute();
        String str = null;
        return  response.body().string();
    }

    private static String urlEncodeToYaDisk(String parameter) {
        String url = null;
        try {
            url = "https://cloud-api.yandex.net:443/v1/disk/public/resources?public_key=" + URLEncoder.encode(parameter, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}

