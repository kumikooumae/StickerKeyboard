package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface PackTagDao {

    @Query("SELECT * FROM " + Tag.TABLE_NAME  + " WHERE " + Tag.ID + " IN " +
            "(SELECT " + PackTag.TAG + " FROM " + PackTag.TABLE_NAME + " WHERE " + PackTag.PACK + " = :packId)")
    List<Tag> getTags(long packId);
}
