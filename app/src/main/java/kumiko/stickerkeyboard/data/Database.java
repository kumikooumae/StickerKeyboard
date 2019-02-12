package kumiko.stickerkeyboard.data;

import android.content.Context;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class Database extends RoomDatabase {

    public abstract StickerDao stickerDao();

    public abstract StickerPackDao stickerPackDao();

    public abstract HistoryDao historyDao();

    public static Database instance;

    private static final int MAX_HISTORIES = 50;

    private ArrayList<History> histories;

    private ArrayList<Sticker> historyStickers;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "sticker.db").build();
        }
        return instance;
    }

    public ArrayList<StickerPack> getAllStickerPacks() {
        ArrayList<StickerPack> packs = stickerPackDao().getAllStickerPacks();
        for (StickerPack pack: packs) {
            pack.stickers = getStickers(pack);
        }
        return packs;
    }

    public ArrayList<Sticker> getStickers(@NonNull StickerPack pack) {
        return stickerDao().getStickers(pack.getId());
    }

    public synchronized ArrayList<Sticker> getHistoryStickersReversed() {
        if (histories == null || historyStickers == null) {
            histories = historyDao().getHistories();
            Collections.reverse(histories);
            historyStickers = new ArrayList<>();
            for (History history: histories) {
                historyStickers.add(stickerDao().getSticker(history.getStickerId()));
            }
        }
        return historyStickers;
    }

    public synchronized void refreshHistory(Sticker sticker) {
        for (int i = 0; i < historyStickers.size(); i++) {
            if (sticker.getId() == historyStickers.get(i).getId()) {
                removeFromHistory(i);
                break;
            }
        }
        if (histories.size() >= MAX_HISTORIES || historyStickers.size() >= MAX_HISTORIES) {
            removeFromHistory(MAX_HISTORIES);
        }
        History newHistory = new History(sticker.getId());
        histories.add(0, newHistory);
        historyStickers.add(0, sticker);
        historyDao().insertHistories(newHistory);
    }

    private void removeFromHistory(int position) {
        History removed = histories.remove(position);
        historyStickers.remove(position);
        historyDao().deleteHistories(removed);
    }

    public ArrayList<StickerPack> addNewEmptyPack(String name) {
        stickerPackDao().insertStickerPacks(new StickerPack(name));
        return getAllStickerPacks();
    }
}
