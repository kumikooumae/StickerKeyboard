package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = History.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Sticker.class,
                parentColumns = Sticker.ID,
                childColumns = History.STICKER_ID,
                onDelete = CASCADE,
                onUpdate = CASCADE
        ))
public class History {

    static final String TABLE_NAME = "history";

    private static final String ID = "id";

    static final String STICKER_ID = "sticker_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public int id;

    @ColumnInfo(name = STICKER_ID)
    public int stickerId;

    public History(int stickerId) {
        this.stickerId = stickerId;
    }

    public int getStickerId() {
        return stickerId;
    }
}
