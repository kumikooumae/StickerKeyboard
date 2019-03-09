package kumiko.stickerkeyboard.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {Sticker.class, StickerPack.class, History.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract StickerDao stickerDao();

    public abstract StickerPackDao stickerPackDao();

    public abstract HistoryDao historyDao();

    private static Database instance;

    private SupportSQLiteDatabase sqldb;

    private List<StickerPack> packs;

    private static final int MAX_HISTORIES = 50;

    private List<History> histories;

    private List<Sticker> historyStickers;

    private static final String TAG = "Database";

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "sticker.db").build();
            instance.sqldb = instance.getOpenHelper().getWritableDatabase();
        }
        return instance;
    }

    public List<StickerPack> getAllStickerPacks() {
        if (packs == null) {
            packs = stickerPackDao().getAllStickerPacks();
            for (StickerPack pack: packs) {
                pack.stickers = stickerDao().getStickers(pack.getId());
            }
        }
        return packs;
    }

    public synchronized List<Sticker> getHistoryStickersReversed() {
        if (histories == null || historyStickers == null) {
            histories = historyDao().getHistoriesReversed();
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
        historyDao().insertHistory(new History(sticker.getId()));
        histories.add(0, historyDao().getLatestInsertedHistory());
        historyStickers.add(0, sticker);
    }

    private void removeFromHistory(int position) {
        History removed = histories.remove(position);
        historyStickers.remove(position);
        historyDao().deleteHistory(removed);
    }

    public synchronized void addNewEmptyPack(String name) {
        stickerPackDao().insertStickerPack(new StickerPack(name));
        packs.add(stickerPackDao().getLastStickerPack());
    }

    public synchronized Sticker addNewSticker(int packId, Sticker.Type type) {
        // fileName is unused
        sqldb.execSQL("INSERT INTO " + Sticker.TABLE_NAME
                + "(" + Sticker.FILE_NAME + "," + Sticker.PACK_ID + "," + Sticker.POSITION + "," + Sticker.TYPE + ") "
                + "VALUES ('', " + packId + ", (SELECT IFNULL(MAX(" + Sticker.ID + "), 0) FROM " + Sticker.TABLE_NAME + ") + 1, " + type.ordinal() +")");
        Sticker sticker = stickerDao().getLatestInsertedSticker();
        for (StickerPack pack: packs) {
            if (pack.getId() == packId) {
                pack.getStickers().add(sticker);
                break;
            }
        }
        return sticker;
    }
}
