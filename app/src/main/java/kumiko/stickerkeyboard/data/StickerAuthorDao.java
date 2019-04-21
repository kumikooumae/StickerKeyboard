package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface StickerAuthorDao {

    @Query("SELECT * FROM " + Author.TABLE_NAME  + " WHERE " + Author.ID + " IN " +
            "(SELECT " + StickerAuthor.AUTHOR + " FROM " + StickerAuthor.TABLE_NAME + " WHERE " + StickerAuthor.STICKER + " = :stickerId)")
    List<Author> getAuthors(long stickerId);
}
