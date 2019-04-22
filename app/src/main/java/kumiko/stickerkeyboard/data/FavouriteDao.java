package kumiko.stickerkeyboard.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavouriteDao {
    @Insert
    long insertFavourite(Favourite favourite);

    @Delete
    void deleteFavourite(Favourite favourite);

    @Update
    void updateFavourite(Favourite favourite);

    @Query("SELECT * FROM " + Favourite.TABLE_NAME)
    List<Favourite> getAllFavourites();
}
