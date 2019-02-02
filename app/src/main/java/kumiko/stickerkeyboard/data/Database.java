package kumiko.stickerkeyboard.data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class Database extends RoomDatabase {

    public abstract StickerDao stickerDao();

    public abstract StickerPackDao stickerPackDao();

    public abstract HistoryDao historyDao();

    public static Database instance;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "sticker.db").build();
        }
        return instance;
    }


}
