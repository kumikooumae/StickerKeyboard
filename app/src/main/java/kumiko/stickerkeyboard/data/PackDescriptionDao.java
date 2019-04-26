package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface PackDescriptionDao {

    @Query("SELECT " + PackDescription.DESCRIPTION + " FROM " + PackDescription.TABLE_NAME + " WHERE " + PackDescription.PACK + " = :pack")
    List<String> getDescriptions(long pack);
}
