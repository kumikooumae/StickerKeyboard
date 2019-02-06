package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

public interface HistoryDao {
    @Insert
    void insertHistories(History... histories);

    @Update
    void updateHistories(History... histories);

    @Delete
    void deleteHistories(History... histories);

    @Query("SELECT * FROM " + History.TABLE_NAME)
    List<History> getHistories();
}
