package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StickerDao {
    @Insert
    long insertSticker(Sticker sticker);

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
    List<Sticker> getStickers(int packId);

    /**
     * Get sticker by stickerId
     * @param stickerId packId
     * @return Sticker. Nullable.
     */
    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.ID + " = :stickerId")
    Sticker getSticker(int stickerId);

    /**
     * Get latest inserted sticker.
     * Call this immediately after insertion to get its rowid.
     * Do not reuse dummy object for insertion, whose rowid is initialized as 0.
     *
     * @return Latest inserted sticker. Nullable.
     */
    @Query("SELECT * FROM "+ Sticker.TABLE_NAME + " ORDER BY " + Sticker.ID + " DESC LIMIT 1")
    Sticker getLatestInsertedSticker();
}
