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

    @Query("SELECT * FROM " + StickerPack.TABLE_NAME)
    List<StickerPack> getAllStickerPacks();

    @Query("SELECT * FROM " + StickerPack.TABLE_NAME + " ORDER BY " + StickerPack.ID + " DESC LIMIT 1")
    StickerPack getLastStickerPack();
}
