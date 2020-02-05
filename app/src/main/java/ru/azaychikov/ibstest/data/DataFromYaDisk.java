package ru.azaychikov.ibstest.data;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataFromYaDisk  {

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static String sendGet(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        CallbackFuture future = new CallbackFuture();
        client.newCall(request).enqueue(future);
        Response response = future.get();
        int statusCode = response.code();
        if(statusCode == 200) {
            return  response.body().string();
        }
        return  null;
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

@RequiresApi(api = Build.VERSION_CODES.N)
class CallbackFuture extends CompletableFuture<Response> implements Callback {
    public void onResponse(Call call, Response response) {
        super.complete(response);
    }
    public void onFailure(Call call, IOException e){
        super.completeExceptionally(e);
    }
}