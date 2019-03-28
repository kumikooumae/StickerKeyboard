package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StickerPackDao {
    @Insert
    void insertStickerPack(StickerPack stickerPack);

    @Update
    void updateStickerPack(StickerPack stickerPack);

    @Delete
    void deleteStickerPack(StickerPack stickerPack);

    /**
     * Get all sticker packs from DB.
     *
     * @return List<StickerPack>. NonNull. If no pack, empty ArrayList is returned.
     */
    @Query("SELECT * FROM " + StickerPack.TABLE_NAME)
    List<StickerPack> getAllStickerPacks();

    /**
     * Get latest inserted sticker pack.
     * Call this immediately after insertion to get its rowid.
     * Do not reuse dummy object for insertion, whose rowid is initialized as 0.
     *
     * @return Latest inserted sticker pack. Nullable.
     */
    @Query("SELECT * FROM " + StickerPack.TABLE_NAME + " ORDER BY " + StickerPack.ID + " DESC LIMIT 1")
    StickerPack getLastStickerPack();
}
