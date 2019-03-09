package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface HistoryDao {
    @Insert
    void insertHistory(History history);

    @Update
    void updateHistory(History history);

    @Delete
    void deleteHistory(History history);

    @Query("SELECT * FROM " + History.TABLE_NAME + " ORDER BY " + History.ID + " DESC")
    List<History> getHistoriesReversed();

    @Query("SELECT * FROM " + History.TABLE_NAME + " ORDER BY " + History.ID + " DESC LIMIT 1")
    History getLatestInsertedHistory();
}
