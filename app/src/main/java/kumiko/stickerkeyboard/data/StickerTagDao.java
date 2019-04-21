package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface StickerTagDao {

    @Query("SELECT * FROM " + Tag.TABLE_NAME  + " WHERE " + Tag.ID + " IN " +
            "(SELECT " + StickerTag.TAG + " FROM " + StickerTag.TABLE_NAME + " WHERE " + StickerTag.STICKER + " = :stickerId)")
    List<Tag> getTags(long stickerId);
}
