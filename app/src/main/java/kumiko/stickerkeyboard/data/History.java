package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = History.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Sticker.class,
                parentColumns = Sticker.ROWID,
                childColumns = History.STICKER_ID,
                onDelete = CASCADE,
                onUpdate = CASCADE
        ))
public class History {

    static final String TABLE_NAME = "history";

    static final String STICKER_ID = "sticker_id";

    @ColumnInfo(name = STICKER_ID)
    public int stickerId;

    public History(int stickerId) {
        this.stickerId = stickerId;
    }

    public int getStickerId() {
        return stickerId;
    }
}
