package kumiko.stickerkeyboard.data;

import java.util.ArrayList;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

public interface StickerPackDao {
    @Insert
    void insertStickerPacks(StickerPack... stickerPacks);

    @Update
    void updateStickerPacks(StickerPack... stickerPacks);

    @Delete
    void deleteStickerPacks(StickerPack... stickerPacks);

    @Query("SELECT * FROM " + StickerPack.TABLE_NAME)
    ArrayList<StickerPack> getAllStickerPacks();
}
