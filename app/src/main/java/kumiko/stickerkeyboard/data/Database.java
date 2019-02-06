package kumiko.stickerkeyboard.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

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

    public List<StickerPack> getAllStickerPacks() {
        return stickerPackDao().getAllStickerPacks();
    }

    public List<Sticker> getStickers(@NonNull StickerPack pack) {
        return stickerDao().getStickers(pack.id);
    }

    public List<Sticker> getHistoryStickers() {
        List<History> histories = historyDao().getHistories();
        ArrayList<Sticker> historyStickers = new ArrayList<>();
        for (History history: histories) {
            historyStickers.add(stickerDao().getSticker(history.stickerId));
        }
        return historyStickers;
    }
}
