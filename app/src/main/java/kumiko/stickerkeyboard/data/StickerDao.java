package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StickerDao {

    @Update
    void updateSticker(Sticker sticker);

    @Delete
    void deleteSticker(Sticker sticker);

    /**
     * Get List<Sticker> by packId
     * @param packId packId
     * @return List<Sticker>. NonNull. If not found, empty ArrayList is returned.
     */
    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.PACK_ID + " = :packId")
    List<Sticker> getStickers(long packId);

    /**
     * Get sticker by stickerId
     * @param stickerId packId
     * @return Sticker. Nullable.
     */
    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.ID + " = :stickerId")
    Sticker getSticker(long stickerId);
}
