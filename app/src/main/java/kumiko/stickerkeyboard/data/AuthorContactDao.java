package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface AuthorContactDao {

    @Query("SELECT * FROM " + AuthorContact.TABLE_NAME + " WHERE " + AuthorContact.AUTHOR + " = :author")
    List<AuthorContact> getAuthorContacts(long author);
}