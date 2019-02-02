package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

public interface StickerDao {
    @Insert
    void insertStickers(Sticker... stickers);

    @Update
    void updateStickers(Sticker... stickers);

    @Delete
    void deleteStickers(Sticker... stickers);

    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.PACK_ID + " = :packId")
    List<Sticker> getStickers(int packId);
}
