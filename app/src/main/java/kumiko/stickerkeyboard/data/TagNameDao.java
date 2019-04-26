package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TagNameDao {

    @Insert
    long insertTagName(TagName tagName);

    @Query("SELECT " + TagName.NAME + " FROM " + TagName.TABLE_NAME + " WHERE " + TagName.TAG + " = :tag")
    List<String> getTagNames(long tag);
}
