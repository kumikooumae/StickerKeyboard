package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface PackAuthorDao {

    @Query("SELECT * FROM " + Author.TABLE_NAME  + " WHERE " + Author.ID + " IN " +
            "(SELECT " + PackAuthor.AUTHOR + " FROM " + PackAuthor.TABLE_NAME + " WHERE " + PackAuthor.PACK + " = :packId)")
    List<Author> getAuthors(long packId);
}
