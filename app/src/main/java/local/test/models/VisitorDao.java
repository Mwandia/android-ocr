package local.test.models;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface VisitorDao {
    @Insert
    void insert(Visitor visitor);

    @Update
    void update(Visitor visitor);

    @Delete
    void delete(Visitor visitor);

    @Query("DELETE * FROM visitors")
    void deleteAllVisitors();

    @Query("SELECT * FROM visitors ORDER_BY entry DESC")
    LiveData<List<Visitor>> getAllVisitors();
}
