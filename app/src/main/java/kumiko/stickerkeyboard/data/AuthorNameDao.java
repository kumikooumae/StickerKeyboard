package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface AuthorNameDao {

    @Query("SELECT " + AuthorName.NAME + " FROM " + AuthorName.TABLE_NAME + " WHERE " + AuthorName.AUTHOR + " = :author")
    List<String> getAuthorNames(long author);
}
