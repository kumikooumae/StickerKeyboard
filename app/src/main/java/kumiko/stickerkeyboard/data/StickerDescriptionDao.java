package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface StickerDescriptionDao {

    @Query("SELECT " + StickerDescription.DESCRIPTION + " FROM " + StickerDescription.TABLE_NAME + " WHERE " + StickerDescription.STICKER + " = :sticker")
    List<String> getDescriptions(long sticker);
}
