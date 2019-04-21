package kumiko.stickerkeyboard.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = Favourite.TABLE_NAME,
        foreignKeys = @ForeignKey(
                entity = Sticker.class,
                parentColumns = Sticker.ID,
                childColumns = Favourite.STICKER_ID,
                onDelete = CASCADE,
                onUpdate = CASCADE
        ))
class Favourite {

    static final String TABLE_NAME = "favourite";

    static final String ID = "id";

    static final String STICKER_ID = "sticker_id";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    private long id;

    @ColumnInfo(name = STICKER_ID)
    private final long stickerId;

    public Favourite(long stickerId) {
        this.stickerId = stickerId;
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    long getStickerId() {
        return stickerId;
    }
}
