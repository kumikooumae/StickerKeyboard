package kumiko.stickerkeyboard.data;

import android.content.Context;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {Sticker.class, StickerPack.class, History.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract StickerDao stickerDao();

    public abstract StickerPackDao stickerPackDao();

    public abstract HistoryDao historyDao();

    private static Database instance;

    private SupportSQLiteDatabase sqldb;

    private static final int MAX_HISTORIES = 50;

    private List<History> histories;

    private List<Sticker> historyStickers;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "sticker.db").build();
            instance.sqldb = instance.getOpenHelper().getWritableDatabase();
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
        return stickerDao().getStickers(pack.getId());
    }

    public synchronized List<Sticker> getHistoryStickersReversed() {
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
        historyDao().insertHistory(newHistory);
    }

    private void removeFromHistory(int position) {
        History removed = histories.remove(position);
        historyStickers.remove(position);
        historyDao().deleteHistory(removed);
    }

    public List<StickerPack> addNewEmptyPack(String name) {
        stickerPackDao().insertStickerPack(new StickerPack(name));
        return getAllStickerPacks();
    }

    public Sticker addNewSticker(int packId, Sticker.Type type) {
        // fileName is unused
        sqldb.execSQL("INSERT INTO " + Sticker.TABLE_NAME
                + "(" + Sticker.FILE_NAME + "," + Sticker.PACK_ID + "," + Sticker.POSITION + "," + Sticker.TYPE + ") "
                + "VALUES ('', " + packId + ", (SELECT IFNULL(MAX(" + Sticker.ID + "), 0) FROM " + Sticker.TABLE_NAME + ") + 1, " + type.ordinal() +")");
//        Sticker sticker = new Sticker("", packId, type);
//        int stickerId = (int) stickerDao().insertSticker(sticker);
//        sticker.setPosition(stickerId);
//        stickerDao().updateSticker(sticker);
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + Sticker.TABLE_NAME + " ORDER BY " + Sticker.ID + " DESC LIMIT 1");
        return stickerDao().getStickerRaw(query);
    }
}
