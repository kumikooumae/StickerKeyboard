package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = StickerAuthor.TABLE_NAME,
        foreignKeys = {
                @ForeignKey(
                        entity = Sticker.class,
                        parentColumns = Sticker.ID,
                        childColumns = StickerAuthor.STICKER,
                        onDelete = CASCADE,
                        onUpdate = CASCADE),
                @ForeignKey(
                        entity = Author.class,
                        parentColumns = Author.ID,
                        childColumns = StickerAuthor.AUTHOR,
                        onDelete = CASCADE,
                        onUpdate = CASCADE)})
class StickerAuthor {

    static final String TABLE_NAME = "sticker_author";

    static final String ID = "id";

    static final String STICKER = "sticker";

    static final String AUTHOR = "author";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = STICKER)
    private final long sticker;

    @ColumnInfo(name = AUTHOR)
    private final long author;

    StickerAuthor(long sticker, long author) {
        this.sticker = sticker;
        this.author = author;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    long getSticker() {
        return sticker;
    }

    long getAuthor() {
        return author;
    }
}
