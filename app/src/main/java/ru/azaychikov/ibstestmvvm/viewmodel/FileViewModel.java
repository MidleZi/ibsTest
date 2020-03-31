package ru.azaychikov.ibstestmvvm.viewmodel;

import android.app.Application;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import ru.azaychikov.ibstestmvvm.model.File;
import ru.azaychikov.ibstestmvvm.repository.FileRepository;

public class FileViewModel extends AndroidViewModel {

    private static String url = "https://yadi.sk/d/G8AQKlUhT47Z_w";
    private FileRepository articleRepository;
    private LiveData<Map<String, List<File>>> filesLiveData;

    public FileViewModel(@NonNull Application application) {
        super(application);

        articleRepository = new FileRepository();
        this.filesLiveData = articleRepository.getAllFiles(application, url);
    }

    public LiveData<Map<String, List<File>>> getFilesLiveData() {
        return filesLiveData;
    }

}
