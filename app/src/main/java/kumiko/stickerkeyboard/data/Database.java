package kumiko.stickerkeyboard.data;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import kumiko.stickerkeyboard.IMEService;
import kumiko.stickerkeyboard.MyApplication;

@androidx.room.Database(entities = {
        Sticker.class, StickerPack.class, History.class,
        StickerDescription.class, PackDescription.class,
        Tag.class, TagName.class, StickerTag.class, PackTag.class,
        Author.class, AuthorName.class, AuthorContact.class, StickerAuthor.class, PackAuthor.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract StickerDao stickerDao();

    public abstract StickerPackDao stickerPackDao();

    public abstract HistoryDao historyDao();

    public abstract TagNameDao tagNameDao();

    public abstract StickerTagDao stickerTagDao();

    public abstract PackTagDao packTagDao();

    public abstract StickerDescriptionDao stickerDescriptionDao();

    public abstract PackDescriptionDao packDescriptionDao();

    public abstract StickerAuthorDao stickerAuthorDao();

    public abstract PackAuthorDao packAuthorDao();

    public abstract AuthorNameDao authorNameDao();

    public abstract AuthorContactDao authorContactDao();

    private static Database instance;

    private SupportSQLiteDatabase sqldb;

    private List<StickerPack> packs;

    private static final int MAX_HISTORIES = 50;

    private List<Sticker> historyStickers;

    public static synchronized Database getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "sticker.db").build();
            instance.sqldb = instance.getOpenHelper().getWritableDatabase();
        }
        return instance;
    }

    @NonNull
    public List<StickerPack> getAllStickerPacks() {
        if (packs == null) {
            packs = stickerPackDao().getAllStickerPacks();
            for (StickerPack pack: packs) {
                pack.stickers = Objects.requireNonNull(stickerDao().getStickers(pack.getId()));
            }
        }
        return packs;
    }

    @NonNull
    public synchronized List<Sticker> getHistoryStickersReversed() {
        if (historyStickers == null) {
            List<History> histories = historyDao().getHistoriesReversed();
            historyStickers = new ArrayList<>();
            for (History history: histories) {
                Sticker historySticker = Objects.requireNonNull(stickerDao().getSticker(history.getStickerId()));
                historySticker.setHistory(history);
                historyStickers.add(historySticker);
            }
        }
        return historyStickers;
    }

    public synchronized void refreshHistory(@NonNull Sticker sticker) {
        for (int i = 0; i < historyStickers.size(); i++) {
            if (sticker.getId() == historyStickers.get(i).getId()) {
                removeFromHistory(i);
                break;
            }
        }
        if (historyStickers.size() >= MAX_HISTORIES) {
            removeFromHistory(MAX_HISTORIES);
        }
        History history = new History(sticker.getId());
        history.setId(historyDao().insertHistory(history));
        sticker.setHistory(history);
        historyStickers.add(0, sticker);
    }

    private void removeFromHistory(int position) {
        Sticker removed = historyStickers.remove(position);
        historyDao().deleteHistory(removed.getHistory());
    }

    public synchronized void addNewEmptyPack(@NonNull String name) {
        StickerPack pack = new StickerPack(name);
        pack.setId(stickerPackDao().insertStickerPack(pack));
        packs.add(pack);
        IMEService.notifyPacksListUpdated(MyApplication.getAppContext());
    }

    @NonNull
    public synchronized Sticker addNewSticker(int packPosition, @NonNull Sticker.Type type) {
        // fileName is unused
        StickerPack pack = packs.get(packPosition);
        Sticker sticker = new Sticker("", pack.getId(), type);
        SupportSQLiteStatement statement = sqldb.compileStatement("INSERT INTO " + Sticker.TABLE_NAME
                + "(" + Sticker.FILE_NAME + "," + Sticker.PACK_ID + "," + Sticker.POSITION + "," + Sticker.TYPE + ") "
                + "VALUES (?, ?, (SELECT IFNULL(MAX(" + Sticker.ID + "), 0) FROM " + Sticker.TABLE_NAME + ") + 1, ?)");
        statement.bindString(1, "");
        statement.bindLong(2, pack.getId());
        statement.bindLong(3, type.ordinal());
        sticker.setId(statement.executeInsert());
        pack.getStickers().add(sticker);
        IMEService.notifyStickerPackUpdated(MyApplication.getAppContext(), packPosition);
        return sticker;
    }

    public synchronized String getTags(Sticker sticker) {
        List<Tag> tags = stickerTagDao().getTags(sticker.getId());
        return fillTags(tags);
    }

    public synchronized String getTags(StickerPack pack) {
        List<Tag> tags = packTagDao().getTags(pack.getId());
        return fillTags(tags);
    }

    private synchronized String fillTags(List<Tag> tags) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Tag tag: tags) {
            List<TagName> tagNames = tagNameDao().getTagNames(tag.getId());
            List<String> names = new ArrayList<>();
            for (TagName tagName: tagNames) {
                names.add(tagName.getName());
            }
            stringBuilder.append(TextUtils.join(",", names));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public synchronized String getDescriptions(Sticker sticker) {
        List<StickerDescription> stickerDescriptions = stickerDescriptionDao().getDescriptions(sticker.getId());
        List<String> strings = new ArrayList<>();
        for (StickerDescription stickerDescription: stickerDescriptions) {
            strings.add(stickerDescription.getDescription());
        }
        return TextUtils.join("\n", strings);
    }

    public synchronized String getDescriptions(StickerPack stickerPack) {
        List<PackDescription> packDescriptions = packDescriptionDao().getDescriptions(stickerPack.getId());
        List<String> strings = new ArrayList<>();
        for (PackDescription packDescription: packDescriptions) {
            strings.add(packDescription.getDescription());
        }
        return TextUtils.join("\n", strings);
    }

    public synchronized String getAuthors(Sticker sticker) {
        List<Author> authors = stickerAuthorDao().getAuthors(sticker.getId());
        return fillAuthors(authors);
    }

    public synchronized String getAuthors(StickerPack stickerPack) {
        List<Author> authors = packAuthorDao().getAuthors(stickerPack.getId());
        return fillAuthors(authors);
    }

    private synchronized String fillAuthors(List<Author> authors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Author author: authors) {
            List<AuthorName> authorNames = authorNameDao().getAuthorNames(author.getId());
            List<String> names = new ArrayList<>();
            for (AuthorName authorName: authorNames) {
                names.add(authorName.getName());
            }
            stringBuilder.append(TextUtils.join(",", names));
            stringBuilder.append("\n");
            List<AuthorContact> authorContacts = authorContactDao().getAuthorContacts(author.getId());
            List<String> contacts = new ArrayList<>();
            for (AuthorContact authorContact: authorContacts) {
                contacts.add(authorContact.getContact());
            }
            stringBuilder.append(TextUtils.join(",", contacts));
            stringBuilder.append("\n\n");
        }
        return stringBuilder.toString();
    }
}
