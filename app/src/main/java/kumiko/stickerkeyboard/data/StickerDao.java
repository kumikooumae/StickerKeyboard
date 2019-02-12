package kumiko.stickerkeyboard.data;

import java.util.ArrayList;
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
    ArrayList<Sticker> getStickers(int packId);

    @Query("SELECT * FROM " + Sticker.TABLE_NAME + " WHERE " + Sticker.ROWID + " = :stickerId")
    Sticker getSticker(int stickerId);
}
