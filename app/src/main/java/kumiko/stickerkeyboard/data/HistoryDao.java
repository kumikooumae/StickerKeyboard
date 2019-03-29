package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface HistoryDao {
    @Insert
    long insertHistory(History history);

    @Delete
    void deleteHistory(History history);

    /**
     * Get List<History> in reverse chronological order (newest at front).
     *
     * @return Reversed List<History>. NonNull. If no history, empty ArrayList is returned.
     */
    @Query("SELECT * FROM " + History.TABLE_NAME + " ORDER BY " + History.ID + " DESC")
    List<History> getHistoriesReversed();
}
