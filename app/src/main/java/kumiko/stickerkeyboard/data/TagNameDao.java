package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TagNameDao {

    @Insert
    long insertTagName(TagName tagName);

    @Query("SELECT * FROM " + TagName.TABLE_NAME + " WHERE " + TagName.TAG + " = :tag")
    List<TagName> getTagNames(long tag);
}
