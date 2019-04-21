package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = StickerTag.TABLE_NAME,
        foreignKeys = {
            @ForeignKey(
                entity = Sticker.class,
                parentColumns = Sticker.ID,
                childColumns = StickerTag.STICKER,
                onDelete = CASCADE,
                onUpdate = CASCADE),
            @ForeignKey(
                entity = Tag.class,
                parentColumns = Tag.ID,
                childColumns = StickerTag.TAG,
                onDelete = CASCADE,
                onUpdate = CASCADE)})
class StickerTag {

    static final String TABLE_NAME = "sticker_tag";

    static final String ID = "id";

    static final String STICKER = "sticker";

    static final String TAG = "tag";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = STICKER)
    private final long sticker;

    @ColumnInfo(name = TAG)
    private final long tag;

    StickerTag(long sticker, long tag) {
        this.sticker = sticker;
        this.tag = tag;
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

    long getTag() {
        return tag;
    }
}
