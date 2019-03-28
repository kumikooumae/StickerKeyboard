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

    /**
     * Get List<History> in reverse chronological order (newest at front).
     *
     * @return Reversed List<History>. NonNull. If no history, empty ArrayList is returned.
     */
    @Query("SELECT * FROM " + History.TABLE_NAME + " ORDER BY " + History.ID + " DESC")
    List<History> getHistoriesReversed();

    /**
     * Get latest inserted sticker.
     * Call this immediately after insertion to get its rowid.
     * Do not reuse dummy object for insertion, whose rowid is initialized as 0.
     *
     * @return Latest inserted history. Nullable.
     */
    @Query("SELECT * FROM " + History.TABLE_NAME + " ORDER BY " + History.ID + " DESC LIMIT 1")
    History getLatestInsertedHistory();
}
