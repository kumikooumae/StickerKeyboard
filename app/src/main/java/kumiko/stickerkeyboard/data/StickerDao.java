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

    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.PACK_ID + " = :packId")
    List<Sticker> getStickers(int packId);

    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.ID + " = :stickerId")
    Sticker getSticker(int stickerId);

    @Query("SELECT * FROM "+ Sticker.TABLE_NAME + " ORDER BY " + Sticker.ID + " DESC LIMIT 1")
    Sticker getLatestInsertedSticker();
}
