package kumiko.stickerkeyboard.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class Database extends RoomDatabase {

    public abstract StickerDao stickerDao();

    public abstract StickerPackDao stickerPackDao();

    public abstract HistoryDao historyDao();

    public static Database instance;

    private static final int MAX_HISTORIES = 50;

    private List<History> histories;

    private List<Sticker> historyStickers;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "sticker.db").build();
        }
        return instance;
    }

    public List<StickerPack> getAllStickerPacks() {
        List<StickerPack> packs = stickerPackDao().getAllStickerPacks();
        for (StickerPack pack: packs) {
            pack.stickers = getStickers(pack);
        }
        return packs;
    }

    public List<Sticker> getStickers(@NonNull StickerPack pack) {
        return stickerDao().getStickers(pack.id);
    }

    public synchronized List<Sticker> getHistoryStickersReversed() {
        if (histories == null || historyStickers == null) {
            histories = historyDao().getHistories();
            Collections.reverse(histories);
            historyStickers = new ArrayList<>();
            for (History history: histories) {
                historyStickers.add(stickerDao().getSticker(history.stickerId));
            }
        }
        return historyStickers;
    }

    public synchronized void refreshHistory(Sticker sticker) {
        for (int i = 0; i < historyStickers.size(); i++) {
            if (sticker.id == historyStickers.get(i).id) {
                removeFromHistory(i);
                break;
            }
        }
        if (histories.size() >= MAX_HISTORIES || historyStickers.size() >= MAX_HISTORIES) {
            removeFromHistory(MAX_HISTORIES);
        }
        History newHistory = new History(sticker.id);
        histories.add(0, newHistory);
        historyStickers.add(0, sticker);
        historyDao().insertHistories(newHistory);
    }

    private void removeFromHistory(int position) {
        History removed = histories.remove(position);
        historyStickers.remove(position);
        historyDao().deleteHistories(removed);
    }
}
