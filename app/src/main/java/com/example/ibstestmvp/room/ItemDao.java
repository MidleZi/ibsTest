package com.example.ibstestmvp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.ibstestmvp.entities.Item;
import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item")
    List<Item> getAll();

    @Query("SELECT * FROM item where folder = :folder and name != :folder")
    List<Item> getAllInFolder(String folder);

    @Query("SELECT name FROM item where type = 'dir'")
    List<String> getFoldersName();

    @Query("SELECT * FROM item where type = 'dir' and path = '/'")
    Item getRootFolderName();

    @Query("SELECT * FROM item WHERE id = :id")
    Item getById(long id);

    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM item")
    void deletdAllItem();

}
