package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface StickerDescriptionDao {

    @Query("SELECT * FROM " + StickerDescription.TABLE_NAME + " WHERE " + StickerDescription.STICKER + " = :sticker")
    List<StickerDescription> getDescriptions(long sticker);
}
