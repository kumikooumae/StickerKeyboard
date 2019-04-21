package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = StickerDescription.TABLE_NAME,
    foreignKeys = @ForeignKey(
        entity = Sticker.class,
        parentColumns = Sticker.ID,
        childColumns = StickerDescription.STICKER,
        onDelete = CASCADE,
        onUpdate = CASCADE))
class StickerDescription {

    static final String TABLE_NAME = "sticker_description";

    static final String ID = "id";

    static final String STICKER = "sticker";

    static final String DESCRIPTION = "description";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = STICKER)
    private final long sticker;

    @ColumnInfo(name = DESCRIPTION)
    private final String description;

    StickerDescription(long sticker, String description) {
        this.sticker = sticker;
        this.description = description;
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

    String getDescription() {
        return description;
    }
}
